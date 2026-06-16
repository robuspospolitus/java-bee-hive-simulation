package Simulation.Model.MovementStrategies;

import Simulation.Model.Board;

import java.awt.Point;

/**
 * Context class that manages and executes the movement strategy for a specific agent.
 */
public class AgentContext {
    private MovementStrategy strategy;
    private String name;
    private Point position;

    /**
     * Creates a new agent movement context with a name, strategy, and starting position.
     * @param name the unique name of the agent
     * @param strategy the initial movement strategy to use
     * @param startPosition the starting coordinates of the agent
     */
    public AgentContext(String name, MovementStrategy strategy, Point startPosition) {
        this.name = name;
        this.strategy = strategy;
        this.position = new Point(startPosition);
    }

    /**
     * Changes the current movement strategy to a new one.
     * @param strategy the new movement strategy to apply
     */
    public void setStrategy(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes the move calculation using the currently assigned strategy.
     * @param board the simulation board
     */
    public void performMove(Board board) {
        strategy.move(name, position, board);
    }

    /**
     * Gets the current position coordinates of the agent.
     * @return the current position point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Updates the current position coordinates of the agent.
     * @param pos the new position point to set
     */
    public void setPosition(Point pos) {
       position = pos;
    }



}
