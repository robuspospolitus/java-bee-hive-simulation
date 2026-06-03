package Simulation.Model.Agents;
import java.awt.Point;

public abstract class Bee {
    int carryCapacity;
    float energy;
    int sightRadius;
    private Point coordinates;
    static int currentNumBees;

    abstract void move();

    abstract Point findDestination();


}
