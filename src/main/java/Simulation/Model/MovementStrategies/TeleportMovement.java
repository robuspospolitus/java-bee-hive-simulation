package Simulation.Model.MovementStrategies;

import Simulation.Model.Board;

import java.awt.*;

public class TeleportMovement implements MovementStrategy{
    private Point target;

    public TeleportMovement(Point target) {
        this.target = target;
    }

    @Override
    public void move(String agentName, Point position, Board board) {
        position.x = target.x;
        position.y = target.y;
        System.out.println(agentName + " hates portals");
    }
}
