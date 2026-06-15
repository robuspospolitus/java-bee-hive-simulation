package Simulation.Model.Agents;

import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.TeleportMovement;

import java.awt.*;

import Simulation.Model.SimulationConfig;

import static Simulation.Model.BoardCells.CellType.*;

public class Forager extends Bee {
    public Forager(int ID, int age, int spawnX, int spawnY) {
        super(ID, age, spawnX, spawnY, new RandomMovement(), "Forager");
        sightRadius = 4;
        carriedPollen = 0;
    }

    @Override
    public void move(Board board) {
        findDestination(board);
        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove(board);
        Point newPos = movementContext.getPosition();

        if (board.isValidMove(newPos.x, newPos.y)) {
            board.moveAgent(this, oldPos, newPos);
        } else {
            boolean dodged = false;

            for (int xOffset = -1; xOffset <= 1 && !dodged; xOffset++) {
                for (int yOffset = -1; yOffset <= 1 && !dodged; yOffset++) {
                    int dodgeX = oldPos.x + xOffset;
                    int dodgeY = oldPos.y + yOffset;

                    if (board.isValidMove(dodgeX, dodgeY)) {
                        Point dodgePos = new Point(dodgeX, dodgeY);
                        movementContext.setPosition(dodgePos);
                        board.moveAgent(this, oldPos, dodgePos);
                        dodged = true;
                    }
                }
            }

            if (!dodged) {
                movementContext.setPosition(oldPos);
            }
        }

        if (newPos.equals(board.getStashDestination(POLLEN_STASH)) && carriedPollen > 0) {
            Hive ul = board.getHive();
            if(ul.depositPollen(carriedPollen) > 0) {
                System.out.println("Forager " + ID + " unloaded " + carriedPollen + " pollen pieces to the hive. Energy: " + energy);
                carriedPollen = 0;
            }
        }

        age++;
        burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_FORAGER);
    }

    protected Point findDestination(Board board) {
        Point currentPos = movementContext.getPosition();
        boolean isInHiveZone = currentPos.x < 12;

        // IN HIVE
        if (isInHiveZone) {
            // eat whenever is in hive
            if (energy < 90.0) {
                Hive ul = board.getHive();
                float energyNeeded = SimulationConfig.ENERGY_FULL - energy;
                int honeyNeeded = (int) Math.ceil(energyNeeded / SimulationConfig.ENERGY_PER_HONEY);
                int toConsume = ul.consumeFood(honeyNeeded);

                if (toConsume > 0) {
                    energy += toConsume * SimulationConfig.ENERGY_PER_HONEY;
                    if (energy > SimulationConfig.ENERGY_FULL) { energy = SimulationConfig.ENERGY_FULL; }
                    System.out.println("Forager " + ID + " ate " + toConsume + " honey. Energy: " + energy);
                }
            }
            // stash pollen
            if (carriedPollen > 0) {
                System.out.println("Forager " + ID + " flies to deposit pollen into storage. Energy: " + energy);
                movementContext.setStrategy(new TargetedMovement(board.getStashDestination(POLLEN_STASH)));
                return board.getStashDestination(POLLEN_STASH);
            }
            // exit
            if (currentPos.equals(board.getHiveExit()) && carriedPollen == 0 && (double)energy > (double)SimulationConfig.ENERGY_THRESHOLD_RETURN) {
                System.out.println("Forager " + ID + " is empty, full and goes through the exit to the meadow. Energy: " + energy);
                Point destination = board.getTeleportDestination(currentPos);
                movementContext.setStrategy(new TeleportMovement(destination));
                return destination;
            }
            // go towards exit
            System.out.println("Forager " + ID + " heads towards the hive exit. Energy: " + energy);
            movementContext.setStrategy(new TargetedMovement(board.getHiveExit()));
            return board.getHiveExit();
        }

        // IN MEADOW
        // return if low energy or full of pollen
        if (energy < SimulationConfig.ENERGY_THRESHOLD_RETURN || carriedPollen >= SimulationConfig.MAX_POLLEN_CAPACITY) {
            System.out.println("Forager " + ID + " returns to the hive due to the state of resources. Energy: " + energy);
            if (currentPos.equals(board.getHiveEntrance())) {
                Point destination = board.getTeleportDestination(currentPos);
                movementContext.setStrategy(new TeleportMovement(destination));
                return destination;
            }
            movementContext.setStrategy(new TargetedMovement(board.getHiveEntrance()));
            return board.getHiveEntrance();
        }
        // see flower
        Point flower = findClosestTarget(board);
        if (flower != null) {
            return flower;
        }
        // nothing in sight, random movement
        System.out.println("Forager "+ID+" cannot see anything, flying randomly... Energy: " + energy);
        movementContext.setStrategy(new RandomMovement());
        return currentPos;
    }

    private Point findClosestTarget(Board board) {
        Point currentPos = movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    Cell cell = grid[checkX][checkY];
                    if (cell != null && cell.hasFlower() && cell.getPollenAmount() > 0 && cell.getAgent() == null) {
                        System.out.println("Forager " + ID + " sees a flower at point [" + checkX + ", " + checkY + "]. Energy: " + energy);
                        movementContext.setStrategy(new TargetedMovement(new Point(checkX, checkY)));
                        return new Point(checkX, checkY);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void interact(Cell cell) {
        if (cell != null && cell.hasFlower() && carriedPollen < SimulationConfig.MAX_POLLEN_CAPACITY) {
            int spaceAvailable = SimulationConfig.MAX_POLLEN_CAPACITY - carriedPollen;
            int amountToTry = Math.min(SimulationConfig.POLLEN_COLLECTION_AMOUNT, spaceAvailable);
            int collectedPollen = cell.takePollen(amountToTry);
            if (collectedPollen > 0) {
                carriedPollen += collectedPollen;
                System.out.println("Forager " + ID + " collected " + collectedPollen + " pollen. Now carries: " + carriedPollen + "/" + SimulationConfig.MAX_POLLEN_CAPACITY+". Energy: " + energy);
            }
        }
    }

    public AgentContext getMovementContext() { return movementContext; }
}