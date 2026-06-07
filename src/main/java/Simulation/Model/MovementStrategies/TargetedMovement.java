package Simulation.Model.MovementStrategies;

import java.awt.*;

public class TargetedMovement implements MovementStrategy {
    private Point target;

    public TargetedMovement(Point target) {
        this.target = target;
    }

    @Override
    public void move(String agentName, Point position) {
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

        position.x = Math.max(0, Math.min(31, position.x));
        position.y = Math.max(0, Math.min(15, position.y));

        double distance = position.distance(target);
        System.out.println(agentName + " leci do celu. Zostało: " + distance);
    }
}
