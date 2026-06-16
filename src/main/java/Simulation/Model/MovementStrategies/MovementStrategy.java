package Simulation.Model.MovementStrategies;

import java.awt.*;
import Simulation.Model.Board;

public interface MovementStrategy {
    /**
     * Calculates and updates the agent's position based on the movement strategy.
     * @param agentName the identifier name of the agent moving
     * @param position the current position point to be modified
     * @param board the simulation board environment
     */
    void move(String agentName, Point position, Board board);
}

