package Simulation.Model.MovementStrategies;

import java.awt.*;

public class TeleportMovement implements MovementStrategy{

    private Point target;

    public TeleportMovement(Point target) {
        this.target = target;
    }

    @Override
    public void move(String agentName, Point position) {

        position.x = target.x;
        position.y = target.y;
        System.out.println(agentName + " nienawidzi portali");
    }
}
