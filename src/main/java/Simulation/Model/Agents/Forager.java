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
    private int spawnX, spawnY;
    private Point spawnPosition;
    private AgentContext movementContext;

    public Forager(int ID, int age,int spawnX, int spawnY) {
        this.sightRadius = 4;
        this.carriedPollen=0;
        this.ID = ID;
        this.age = age;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnPosition = new Point(spawnX,spawnY);
        this.movementContext = new AgentContext("Forager"+ID, new RandomMovement(), this.spawnPosition);
        totalNumBees++;
    }

    public void collectPollen(int amount) {
        int spaceAvailable = SimulationConfig.MAX_POLLEN_CAPACITY - this.carriedPollen;
        int amountToTake = Math.min(amount, spaceAvailable);

        this.carriedPollen += amountToTake;
        System.out.println("Zbieraczka " + ID + " zebrala pylek. Posiada teraz: " + carriedPollen);
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
            // 2. TRAFFIC JAM! Try to sidestep instead of giving up!
            boolean dodged = false;

            // Look at the 8 adjacent tiles surrounding the bee
            for (int xOffset = -1; xOffset <= 1 && !dodged; xOffset++) {
                for (int yOffset = -1; yOffset <= 1 && !dodged; yOffset++) {

                    int dodgeX = oldPos.x + xOffset;
                    int dodgeY = oldPos.y + yOffset;

                    // If we find an empty tile nearby, slide into it!
                    if (board.isValidMove(dodgeX, dodgeY)) {
                        Point dodgePos = new Point(dodgeX, dodgeY);
                        movementContext.setPosition(dodgePos);
                        board.moveAgent(this, oldPos, dodgePos);
                        dodged = true; // Mark as successful to stop the loop
                    }
                }
            }

            // 3. If completely surrounded on all 8 sides, just wait patiently
            if (!dodged) {
                movementContext.setPosition(oldPos);
            }
        }


        this.age++;
        this.burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_FORAGER);

        // stash pollen
        if (newPos.equals(board.getStashDestination(POLLEN_STASH)) && this.carriedPollen > 0) {
            Hive ul = board.getHive();
            ul.setPollenAmount(ul.getPollenAmount() + this.carriedPollen);
            System.out.println(" Zbieraczka " + ID + " rozładowała " + this.carriedPollen + " szt. pyłku do ula. Razem w ulu: " + ul.getPollenAmount());
            this.carriedPollen = 0;
        }
        // eat
        Point stashPos = board.getStashDestination(HONEY_STASH);
        Point currentPos = movementContext.getPosition();

        boolean isAtStash = Math.abs(currentPos.x - stashPos.x) <= 1 && Math.abs(currentPos.y - stashPos.y) <= 1;

        if (isAtStash && this.energy <= SimulationConfig.ENERGY_THRESHOLD_RETURN) {
            Hive ul = board.getHive();
            int foodAvailable = ul.getHoneyAmount();

            if (foodAvailable > 0) {
                int energyNeeded = 100 - (int)this.energy;
                int toConsume = Math.min(energyNeeded, foodAvailable);

                ul.setHoneyAmount(foodAvailable - toConsume);
                this.energy += toConsume;

                System.out.println("Zbieraczka " + ID + " zjadła " + toConsume + " miodu. Zostało miodu w ulu: " + ul.getHoneyAmount());
            }
        }

    }

    protected Point findDestination(Board board) {
        Point currentPos = this.movementContext.getPosition();
        boolean isInHiveZone = currentPos.x < 12;

        // IN HIVE
        if (isInHiveZone) {
            // teleport to meadow
            if (currentPos.equals(board.getHiveExit())) {
                if (this.carriedPollen == 0 && this.energy > SimulationConfig.ENERGY_THRESHOLD_RETURN) {
                    System.out.println("Zbieraczka " + ID + " jest pusta, najedzona i przechodzi przez wyjscie na lake");
                    Point destination = board.getTeleportDestination(currentPos);
                    movementContext.setStrategy(new TeleportMovement(destination));
                    return destination;
                }
            }
            // stash pollen
            if (this.carriedPollen > 0) {
                System.out.println("Zbieraczka " + ID + " leci zlozyc pylek do magazynu");
                movementContext.setStrategy(new TargetedMovement(board.getStashDestination(POLLEN_STASH)));
                return board.getStashDestination(POLLEN_STASH);
            }
            // eat
            if (this.energy <= SimulationConfig.ENERGY_THRESHOLD_RETURN) {
                System.out.println("Zbieraczka " + ID + " jest glodna i leci do magazynu z miodem");
                movementContext.setStrategy(new TargetedMovement(board.getStashDestination(HONEY_STASH)));
                return board.getStashDestination(HONEY_STASH);
            }
            // exit
            System.out.println("Zbieraczka " + ID + " kieruje sie do wyjscia z ula");
            movementContext.setStrategy(new TargetedMovement(board.getHiveExit()));
            return board.getHiveExit();

        } else { //MEADOW
            // Low energy or full capacity
            if (this.getEnergy() < SimulationConfig.ENERGY_THRESHOLD_RETURN || this.carriedPollen >= SimulationConfig.MAX_POLLEN_CAPACITY) {
                System.out.println("Zbieraczka " + ID + " leci do portalu wejściowego");
                if (currentPos.equals(board.getHiveEntrance())) {
                    System.out.println("Zbieraczka " + ID + " przechodzi przez portal do wnętrza ula");
                    Point destination = board.getTeleportDestination(currentPos);
                    movementContext.setStrategy(new TeleportMovement(destination));
                    return destination;
                }
                movementContext.setStrategy(new TargetedMovement(board.getHiveEntrance()));
                return board.getHiveEntrance();
            }

            // Looking for flowers
            Point flower = findClosestTarget(board);
            if (flower != null) {
                return flower;
            }

            // Nothing in sight
            System.out.println("Nic nie widzę, lecę losowo...");
            movementContext.setStrategy(new RandomMovement());
            return currentPos;
        }
    }

    private Point findClosestTarget(Board board) {
        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    Cell cell = grid[checkX][checkY];
                    if (cell != null && cell.hasFlower() && cell.getPollenAmount() > 0 && cell.getAgent() == null) {
                        System.out.println("Zbieraczka " + ID + " widzi kwiat w punkcie [" + checkX + ", " + checkY + "]");
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
        if (cell != null && cell.hasFlower() && this.carriedPollen < SimulationConfig.MAX_POLLEN_CAPACITY) { //ograniczenie ilosci niesionego pylku
            int spaceAvailable = SimulationConfig.MAX_POLLEN_CAPACITY - this.carriedPollen; //ile miejsca na pylek zostalo
            int amountToTry = Math.min(SimulationConfig.POLLEN_COLLECTION_AMOUNT, spaceAvailable); //bierze tyle ile moze
            int collectedPollen = cell.takePollen(amountToTry);
            if (collectedPollen > 0) {
                this.carriedPollen += collectedPollen;
                System.out.println("Zbieraczka " + ID + " zebrała " + collectedPollen + " pyłku. Posiada teraz: " + carriedPollen + "/" + SimulationConfig.MAX_POLLEN_CAPACITY);
            }
        }
    }

    public AgentContext getMovementContext() { return this.movementContext; }
}



/*
public void interact(Cell cell) {
    if (cell != null && cell.hasFlower()) {
        int collectedPollen = cell.takePollen(SimulationConfig.POLLEN_COLLECTION_AMOUNT);
        if (collectedPollen > 0) {
            this.carriedPollen += collectedPollen;
            System.out.println("Zbieraczka " + ID + " zebrała " + collectedPollen + " pyłku. Posiada teraz: " + carriedPollen + "/" + SimulationConfig.MAX_POLLEN_CAPACITY);
}
}
 */