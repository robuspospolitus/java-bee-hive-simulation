package Simulation.Model.Agents;

import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.TeleportMovement;

import java.awt.*;

import static Simulation.Model.BoardCells.CellType.HIVE;
import static Simulation.Model.BoardCells.CellType.POLLEN_STASH;

public class Forager extends Bee {
    private int numPollen=10, carriedPollen, spawnX, spawnY;
    private Point spawnPosition;

    protected int sightRadius = 4;
    private AgentContext movementContext;

    public Forager(int ID, int age,int spawnX, int spawnY) {
        this.ID = ID;
        this.carriedPollen=0;
        this.age = age;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnPosition = new Point(spawnX,spawnY);
        this.movementContext = new AgentContext("Forager"+ID, new RandomMovement(), this.spawnPosition);
        totalNumBees++;
    }

    public void collectPollen(int amount) {
        this.carriedPollen += amount;
        System.out.println("Zbieraczka " + ID + " zebrala pylek. Posiada teraz: " + carriedPollen);
    }

    @Override
    public void move(Board board) {
        findDestination(board);
        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove();
        Point newPos = movementContext.getPosition();

          if (board.isValidMove(newPos.x, newPos.y)) {
            board.moveAgent(this, oldPos, newPos);
            System.out.println("Zbieraczka porusza sie na: X: "+ newPos.x + ", Y: " + newPos.y);
        } else {
            movementContext.setPosition(oldPos);
            System.out.println("Bee" + ID + " bumped into an obstacle.");
        }

        this.age++;
        this.burnEnergy(1.0f);

        if (newPos.equals(board.getStashDestination(POLLEN_STASH)) && this.carriedPollen > 0) { //wspolrzedne ula
            Hive ul = board.getHive();

            //przekazujemy cały zebrany pyłek do ula
            ul.setPollenAmount(ul.getPollenAmount() + this.carriedPollen);
            System.out.println(" Zbieraczka " + ID + " rozładowała " + this.carriedPollen + " szt. pyłku do ula!");
            this.carriedPollen = 0;

            //Skoro jest w ulu to je
            if (ul.getFoodAmount() > 0) {
                ul.setFoodAmount(ul.getFoodAmount() - 1); // zjada jedzenie ula
                this.setEnergy(100);
                System.out.println(" Zbieraczka " + ID + " zjadła");
            } else {
                System.out.println(" Zbieraczka " + ID + " jest w ulu, ale glodna");
            }
        }
    }

    protected Point findDestination(Board board) {
        Point currentPos = this.movementContext.getPosition();
        Cell currentCell = board.getCell(currentPos.x, currentPos.y);

        if (this.getEnergy() < 25.0f || carriedPollen >= 10) {
            System.out.println("Zbieraczka " + ID + " wraca do ula");

            if (currentPos.equals(board.getHiveEntrance())) {
                System.out.println("Teleporting");
                movementContext.setStrategy(new TeleportMovement(board.getTeleportDestination(currentPos)));
                return board.getTeleportDestination(currentPos);
            }
            if (currentCell.getType() == CellType.MEADOW) {
                System.out.println("Going back to hive");
                movementContext.setStrategy(new TargetedMovement(board.getHiveEntrance()));
                return board.getHiveEntrance();
            }
            if (currentCell.getType() == CellType.HIVE) {
                System.out.println("About to stash pollen");
                Point stash = board.getStashDestination(CellType.POLLEN_STASH);
                movementContext.setStrategy(new TargetedMovement(stash));
                return stash;
            }
        }

        // Looking for flowers
        Point flower = findClosestTarget(board, CellType.MEADOW); // zakładam, że Meadow zawiera kwiaty
        if (flower != null) {
            return flower;
        }

        // Nothing in sight
        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());
        return new Point(1, 1);
    }

    private Point findClosestTarget(Board board, CellType typeToFind) {
        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < board.getWidth() && checkY >= 0 && checkY < board.getHeight()) {
                    Cell cell = grid[checkX][checkY];
                    if (cell != null && cell.getType() == typeToFind && !cell.isEmpty()) {
                        System.out.println("Zbieraczka " + ID + " widzi " + typeToFind + " w punkcie [" + checkX + ", " + checkY + "]");
                        movementContext.setStrategy(new TargetedMovement(new Point(checkX, checkY)));
                        return new Point(checkX, checkY);
                    }
                }
            }
        }
        return null;
    }


    public AgentContext getMovementContext() { return this.movementContext; }

}