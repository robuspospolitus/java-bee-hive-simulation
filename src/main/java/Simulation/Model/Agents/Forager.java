package Simulation.Model.Agents;

import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;

import java.awt.*;

import static Simulation.Model.BoardCells.CellType.HIVE;

public class Forager extends Bee {
    int numPollen=10;
    int carriedPollen;
    int age;
    int spawnX;
    int spawnY;
    Point spawnPosition;

    protected int sightRadius = 3;
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

            if(grid[currentPos.x][currentPos.y].getType()==CellType.TELEPORT){
               // movementContext.setStrategy(new TargetedMovement(board.getTeleportDestination(currentPos)));
                return board.getTeleportDestination(currentPos);
            }

            if(grid[currentPos.x][currentPos.y].getType()==CellType.HIVE){
                movementContext.setStrategy(new TargetedMovement(board.getStashDestination()));
                return board.getTeleportDestination(currentPos);
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
        }


        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());
        return new Point(1, 1);
    }

    public AgentContext getMovementContext() { return this.movementContext; }

}