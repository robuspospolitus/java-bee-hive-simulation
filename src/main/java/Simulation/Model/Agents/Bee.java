package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;

import java.awt.Point;

public abstract class Bee {
    int carryCapacity;
    float energy;
    int sightRadius;
    //private Point coordinates;
    static int totalNumBees;
    protected int ID;
    protected int age;

    public Bee() {
        this.energy = 100.0f;
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

}
