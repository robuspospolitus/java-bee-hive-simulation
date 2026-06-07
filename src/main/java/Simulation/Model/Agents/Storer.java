package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;

import java.awt.*;

public class Storer extends Bee{
    int age;
    int spawnX;
    int spawnY;
    Point spawnPosition;

    private AgentContext movementContext;

    public Storer (int ID, int age, int spawnX, int spawnY){
        this.ID = ID;
        this.age = age;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnPosition = new Point(spawnX, spawnY);
        this.movementContext = new AgentContext("Storer", new RandomMovement());
        totalNumBees++;
    }

    @Override
    public void move(Board board){
        movementContext.performMove();
        System.out.println ("Sprzątaczka się porusza");
    }

    protected Point findDestination(Board board){
        System.out.println ("Sprzątaczka porusza sie po ulu");
        movementContext.setStrategy(new RandomMovement());
        return new Point(spawnX, spawnY);
    }
}
