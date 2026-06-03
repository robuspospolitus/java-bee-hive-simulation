package Simulation.Model.MovementStrategies;

import java.awt.Point;

public class AgentContext {
    private MovementStrategy strategy;
    private String name;
    private Point position;


    public AgentContext(String name, MovementStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.position = new Point(0, 0);
    }

    public void setStrategy(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    public void performMove() {
        strategy.move(name, position);
    }

    public Point getPosition() {
        return this.position;
    }

}
