package Simulation.Model.Agents;

import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.TeleportMovement;

import java.awt.*;

import static Simulation.Model.BoardCells.CellType.HIVE;
import static Simulation.Model.BoardCells.CellType.POLLEN_STASH;

public class Forager extends Bee {
    int numPollen=10;
    int carriedPollen;
    int age;
    int spawnX;
    int spawnY;
    Point spawnPosition;

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

        board.moveAgent(this, oldPos, newPos);
        System.out.println("Zbieraczka porusza sie na: "+ "X: "+ newPos.x + ", Y: " + newPos.y);
    }

    protected Point findDestination(Board board) {
        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        if (carriedPollen >= numPollen) {
            System.out.println("Going back to hive");
            if(grid[currentPos.x][currentPos.y].getType()==CellType.MEADOW){
            movementContext.setStrategy(new TargetedMovement(board.getHiveEntrance()));
            return board.getHiveEntrance();}


            if(grid[currentPos.x][currentPos.y].equals (grid [(board.getHiveEntrance()).x][board.getHiveEntrance().y])){
                System.out.println("Teleporting");
               movementContext.setStrategy(new TeleportMovement(board.getTeleportDestination(currentPos)));
                return board.getTeleportDestination(currentPos);
            }

            if(grid[currentPos.x][currentPos.y].getType()==CellType.HIVE){
                System.out.println("About to stash pollen");
                if(lookForCell(board, POLLEN_STASH)!=null){
                    movementContext.setStrategy(new TargetedMovement(lookForCell(board, POLLEN_STASH)));
                    return (lookForCell(board, POLLEN_STASH));
                }
            }

        }

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    if (grid[checkX][checkY] != null && !grid[checkX][checkY].isEmpty()&& grid[checkX][checkY].hasFlower()){
                        System.out.println("I'm seeing a flower at ("+ checkX +", "+ checkY+")");
                        movementContext.setStrategy(new TargetedMovement(new Point(checkX, checkY)));
                        return new Point(checkX, checkY);
                    }
                }
            }
            return null;
        }

        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());
        return new Point(1, 1);
    }

    private Point lookForCell(Board board, CellType destinationType){
        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    if (grid[checkX][checkY] != null && !grid[checkX][checkY].isEmpty()&& grid[checkX][checkY].getType()==destinationType){
                        System.out.println("I'm seeing a "+ destinationType+ " at ("+ checkX +", "+ checkY+")");
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