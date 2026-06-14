package Simulation.Model.Agents;

import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.TeleportMovement;

import java.awt.*;

import Simulation.Model.SimulationConfig;

import static Simulation.Model.BoardCells.CellType.*;

public class Forager extends Bee {
    public Forager(int ID, int age, int spawnX, int spawnY) {
        super(ID, age, spawnX, spawnY, new RandomMovement(), "Zbieraczka");
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
            ul.setPollenAmount(ul.getPollenAmount() + carriedPollen);
            System.out.println(" Zbieraczka " + ID + " rozładowała " + carriedPollen + " szt. pyłku do ula. Razem w ulu: " + ul.getPollenAmount() + ". Energia: " + energy);
            carriedPollen = 0;
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
                int foodAvailable = ul.getHoneyAmount();

                if (foodAvailable > 0) {
                    int energyNeeded = 100 - (int) energy;
                    int toConsume = Math.min(energyNeeded, foodAvailable);

                    ul.setHoneyAmount(foodAvailable - toConsume);
                    energy += toConsume;

                    System.out.println("Zbieraczka " + ID + " zjadła " + toConsume + " miodu. Aktualna energia: " + energy);
                }
            }
            // stash pollen
            if (carriedPollen > 0) {
                System.out.println("Zbieraczka " + ID + " leci zlozyc pylek do magazynu. Energia: " + energy);
                movementContext.setStrategy(new TargetedMovement(board.getStashDestination(POLLEN_STASH)));
                return board.getStashDestination(POLLEN_STASH);
            }
            // exit
            if (currentPos.equals(board.getHiveExit()) && carriedPollen == 0 && (double)energy > (double)SimulationConfig.ENERGY_THRESHOLD_RETURN) {
                System.out.println("Zbieraczka " + ID + " jest pusta, najedzona i przechodzi przez wyjscie na lake. Energia: " + energy);
                Point destination = board.getTeleportDestination(currentPos);
                movementContext.setStrategy(new TeleportMovement(destination));
                return destination;
            }
            // go towards exit
            System.out.println("Zbieraczka " + ID + " kieruje sie do wyjscia z ula. Energia: " + energy);
            movementContext.setStrategy(new TargetedMovement(board.getHiveExit()));
            return board.getHiveExit();
        }

        // IN MEADOW
        // return if low energy or full of pollen
        if (energy < SimulationConfig.ENERGY_THRESHOLD_RETURN || carriedPollen >= SimulationConfig.MAX_POLLEN_CAPACITY) {
            System.out.println("Zbieraczka " + ID + " wraca do ula ze względu na stan zasobów. Energia: " + energy);
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
        System.out.println("Nic nie widzę, lecę losowo... Energia: " + energy);
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
                        System.out.println("Zbieraczka " + ID + " widzi kwiat w punkcie [" + checkX + ", " + checkY + "]. Energia: " + energy);
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
                System.out.println("Zbieraczka " + ID + " zebrała " + collectedPollen + " pyłku. Posiada teraz: " + carriedPollen + "/" + SimulationConfig.MAX_POLLEN_CAPACITY+". Energia: " + energy);
            }
        }
    }

    public AgentContext getMovementContext() { return movementContext; }
}