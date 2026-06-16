package Simulation.Model.MovementStrategies;

import Simulation.Logger.Logger;
import Simulation.Model.Board;

import java.awt.*;

/**
 * Movement strategy that instantly teleports an agent to a target location.
 */
public class TeleportMovement implements MovementStrategy{
    private Point target;

    /**
     * Creates a teleport movement strategy with a defined destination.
     * @param target the target coordinates to teleport to
     */
    public TeleportMovement(Point target) {
        this.target = target;
    }

    /**
     * Instantly updates the agent's position to the target coordinates.
     * @param agentName the identifier name of the agent
     * @param position the current position to modify
     * @param board the simulation board
     */
    @Override
    public void move(String agentName, Point position, Board board) {
        position.x = target.x;
        position.y = target.y;
        Logger.log(agentName + " hates portals");
    }
}
