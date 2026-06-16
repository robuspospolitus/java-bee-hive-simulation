package Simulation.Model.MovementStrategies;

import Simulation.Model.Board;

import java.awt.Point;

public class AgentContext {
    private MovementStrategy strategy;
    private String name;
    private Point position;

    public AgentContext(String name, MovementStrategy strategy, Point startPosition) {
        this.name = name;
        this.strategy = strategy;
        this.position = new Point(startPosition);
    }

    public void setStrategy(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    public void performMove(Board board) {
        strategy.move(name, position, board);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) {
       position = pos;
    }

    public MovementStrategy getStrategy(){return this.strategy;}


}
