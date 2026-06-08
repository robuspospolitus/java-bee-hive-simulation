package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;

import java.awt.Point;

public abstract class Bee {
    int carryCapacity;
    float energy;
    int sightRadius;
    //private Point coordinates;
    static int totalNumBees = 0;
    protected int ID;
    protected int age;

    protected AgentContext movementContext; //i tak kazda pszczola musi to miec

    public Bee(int ID, int age, int startX, int startY) {
        this.ID = ID;
        this.age = age;
        this.energy = 100.0f; // Każda pszczoła startuje z pełną energią
        totalNumBees++;       // Automatycznie zwiększamy licznik przy narodzinach
    }

    public void update(Board board){
        this.age++;
        this.burnEnergy(1.0f);

        if(!this.isDead()){
            this.move(board);
            this.interactWithEnvironment(board);
        }
    }

    public void burnEnergy(float amount){
        this.energy -= amount;
        if(this.energy < 0){
            this.energy = 0;
        }
    }

    protected void interactWithEnvironment(Board board) {

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
        return this.movementContext;
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
