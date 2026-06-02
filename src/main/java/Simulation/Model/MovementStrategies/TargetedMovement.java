package Simulation.Model.MovementStrategies;

import java.awt.*;

public class TargetedMovement implements MovementStrategy {

    @Override
    public void move(String agentName, Point position){
        Point target = new Point (50,50); //przykladowy shit

        if(position.x < target.x){
            position.x++;
        }
        else if (position.x > target.x){
            position.x--;
        }

        if(position.y < target.y){
            position.y++;
        }
        else if (position.y > target.y){
            position.y--;
        }
        double distance = position.distance(target);
        System.out.println(agentName + " leci do celu. Zostało: " + distance);
    }
}
