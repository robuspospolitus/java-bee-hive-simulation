package Simulation.Model.Agents;

import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
//import Simulation.Model.Cell;

import java.awt.*;

public class Forager extends Bee {
    int numPollen;
    int carriedPollen;
    int age;
    //private Point coordinates;
    private AgentContext movementContext;


    public Forager (int age) {
        this.age = age;
        this.movementContext = new AgentContext("Forager", new RandomMovement());
    }

    @Override
    protected void move(){
        movementContext.performMove();
        System.out.println ("Zbieraczka się porusza");
    }

    protected void findDestination(Board board){
        //Point targetPosition = new Point (1,1);
        //System.out.println ("Zbieraczka znajduje destynację");
        //return targetPosition;
        if(carriedPollen >= 10){
            System.out.println("Going back to hive");
            movementContext.setStrategy(new TargetedMovement());
            return;
        }

        Point currentPos = this.movementContext.getPosition();

        Cell[][] grid = board.getGrid();

        for(int x = -sightRadius; x <= sightRadius; x++){
            for(int y=-sightRadius; y <= sightRadius; y++){
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if(checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length){
                    if(grid[checkX][checkY].hasFlower == true) {
                        System.out.println("I'm seeing a flower");
                        movementContext.setStrategy(new TargetedMovement());
                        return;
                    }
                }
            }
        }

        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());

    }
}
