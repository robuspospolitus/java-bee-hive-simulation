package Simulation.Model.Agents;

import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;

import java.awt.*;

public class Forager extends Bee {
    int numPollen;
    int carriedPollen;
    int age;

    protected int sightRadius = 3;
    private AgentContext movementContext;

    public Forager(int age) {
        this.age = age;
        this.movementContext = new AgentContext("Forager", new RandomMovement());
    }

    @Override
    public void move(Board board) {
        movementContext.performMove();
        System.out.println("Zbieraczka się porusza");
    }

    protected Point findDestination(Board board) {
        if (carriedPollen >= 10) {
            System.out.println("Going back to hive");
            movementContext.setStrategy(new TargetedMovement());
            return new Point(0, 0);
        }

        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    if (grid[checkX][checkY] != null && !grid[checkX][checkY].isEmpty()) {
                        System.out.println("I'm seeing a flower");
                        movementContext.setStrategy(new TargetedMovement());
                        return new Point(checkX, checkY);
                    }
                }
            }
        }


        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());
        return new Point(1, 1);
    }
}