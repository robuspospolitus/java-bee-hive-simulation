package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public abstract class Bee {
    protected int carryCapacity, carriedPollen;
    protected float energy;
    protected int sightRadius;
    static int totalNumBees;
    protected int ID;
    protected int age;

    public Bee() {
        this.energy = SimulationConfig.ENERGY_FULL;
        this.carryCapacity = SimulationConfig.MAX_POLLEN_CAPACITY;
    }

    public void burnEnergy(float amount){
        this.energy -= amount;
        if(this.energy < 0){
            this.energy = 0;
        }
    }

    public boolean isDead(){
        return this.energy <=0;
    }

    public Point getBeePosition() {
        if (this.getMovementContext() != null) {
            return this.getMovementContext().getPosition();
        }
        return null;
    }

    public abstract void move(Board board);

    abstract Point findDestination(Board board);

    public int getID (){
        return ID;
    };

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
        return this.age;
    }

    public void interact(Cell cell) { }

}
