package Simulation.Model.Agents;

import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.BoardCells.Cell;
import java.awt.*;

public class Forager extends Bee {
    private int numPollen = 10;
    private int carriedPollen = 0;

    public Forager(int ID, int age, int spawnX, int spawnY) {
        super(ID, age, spawnX, spawnY);
        this.movementContext = new AgentContext("Forager" + ID, new RandomMovement(), new Point(spawnX, spawnY));
        this.sightRadius = 3;
    }

    public void collectPollen(int amount) {
        this.carriedPollen += amount;
        System.out.println("Zbieraczka " + getID() + " zebrala pylek. Posiada teraz: " + carriedPollen);
    }

    // tylko poruszanie sie
    @Override
    public void move(Board board) {
        findDestination(board);

        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove();

        Point newPos = movementContext.getPosition();
        board.moveAgent(this, oldPos, newPos);
    }

    //proba laczenia kodu Tosi i Klaudii ale to tak roboczo narazie :


    // logika pracy i jedzenia w ulu
    @Override
    protected void interactWithEnvironment(Board board) {
        Point currentPos = movementContext.getPosition();

        // sprawdzamy czy jestesmy  w ulu
        if (board.getCell(currentPos.x, currentPos.y).getType() == CellType.HIVE) {
            Hive ul = board.getHive();

            // Rozladunek
            if (this.carriedPollen > 0) {
                ul.setPollenAmount(ul.getPollenAmount() + this.carriedPollen);
                System.out.println(" Zbieraczka " + getID() + " rozładowała " + this.carriedPollen + " szt. pyłku do ula!");
                this.carriedPollen = 0;
            }

            // jemy
            if (this.getEnergy() < 100.0f) {
                if (ul.getFoodAmount() > 0) {
                    ul.setFoodAmount(ul.getFoodAmount() - 1);
                    this.setEnergy(100.0f);
                    System.out.println(" Zbieraczka " + getID() + " zjadła i odnowiła energię do 100!");
                } else {
                    System.out.println(" Zbieraczka " + getID() + " jest w ulu, ale nie ma jedzenia!");
                }
            }
        }
    }

    @Override
    protected Point findDestination(Board board) {
        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        if (this.getEnergy() < 25.0f || this.carriedPollen >= this.numPollen) {
            System.out.println("Zbieraczka " + getID() + " wraca do ula (plecak pełny lub mało energii)");
            CellType currentCellType = grid[currentPos.x][currentPos.y].getType();

            if (currentCellType == CellType.MEADOW) {
                movementContext.setStrategy(new TargetedMovement(board.getHiveEntrance()));
                return board.getHiveEntrance();
            }
            if (currentCellType == CellType.TELEPORT) {
                Point teleportDest = board.getTeleportDestination(currentPos);
                movementContext.setStrategy(new TargetedMovement(teleportDest));
                return teleportDest;
            }
            if (currentCellType == CellType.HIVE) {
                Point stashDest = board.getStashDestination();
                movementContext.setStrategy(new TargetedMovement(stashDest));
                return stashDest;
            }
        }


        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    Cell cell = grid[checkX][checkY];
                    if (cell != null && !cell.isEmpty() && cell.hasFlower()) {
                        System.out.println("Zbieraczka " + getID() + " widzi kwiatek na (" + checkX + ", " + checkY + ")");
                        Point flowerPos = new Point(checkX, checkY);
                        movementContext.setStrategy(new TargetedMovement(flowerPos));
                        return flowerPos;
                    }
                }
            }
        }

        System.out.println("Zbieraczka " + getID() + " nic nie widzi, lata losowo...");
        movementContext.setStrategy(new RandomMovement());
        return currentPos;
    }

    @Override
    public AgentContext getMovementContext() {
        return this.movementContext;
    }
}