package Simulation.Model.Agents;
import Simulation.Model.Board;

import java.awt.Point;

public abstract class Bee {
    int carryCapacity;
    float energy;
    int sightRadius;
    private Point coordinates;
    static int currentNumBees;

    public abstract void move(Board board);

    abstract Point findDestination(Board board);


}
