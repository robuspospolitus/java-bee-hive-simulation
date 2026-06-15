package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.MovementStrategy;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public abstract class Bee {
    protected int carryCapacity, carriedPollen;
    protected float energy;
    protected int sightRadius;
    static int totalNumBees;
    protected int ID;
    protected int age;
    protected AgentContext movementContext;
    protected Point spawnPosition;

    public Bee(int ID, int age, int x, int y, MovementStrategy strategy, String prefix) {
        this.ID = ID;
        this.age = age;
        spawnPosition = new Point(x, y);
        movementContext = new AgentContext(prefix + " " + ID, strategy, spawnPosition);
        energy = SimulationConfig.ENERGY_FULL;
        carryCapacity = SimulationConfig.MAX_POLLEN_CAPACITY;
        totalNumBees++;
    }

    public void burnEnergy(float amount){
        energy -= amount;
        if(energy < 0){
            energy = 0;
        }
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public boolean isTooOld() {
        return age > SimulationConfig.MAX_BEE_AGE;
    }

    public Point getBeePosition() {
        if (getMovementContext() != null) {
            return getMovementContext().getPosition();
        }
        return null;
    }

    public abstract void move(Board board);

    abstract Point findDestination(Board board);

    public int getID (){
        return ID;
    }

    public static int getTotalNum(){
        return totalNumBees;
    }

    public AgentContext getMovementContext() {
        return null;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public int getAge() {
        return age;
    }

    public void interact(Cell cell) { }

    // ensuring that testing doesn't interfere with counter
    public static void resetTotalNumBeesForTesting() {
        totalNumBees = 0;
    }

}
