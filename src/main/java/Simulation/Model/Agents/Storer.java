package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.SimulationConfig;

import java.awt.*;

public class Storer extends Bee{
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
        this.movementContext = new AgentContext("Storer" + ID, new RandomMovement(), this.spawnPosition);
        totalNumBees++;
    }

    @Override
    public void move(Board board){
        findDestination(board);
        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove(board);
        Point newPos = movementContext.getPosition();
        board.moveAgent(this, oldPos, newPos);
        System.out.println("Magazynierka " + ID + " poruszyla sie");

        this.age++;
        this.burnEnergy(1.0f);
        zasoby(board);
    }

    protected Point findDestination(Board board){
        System.out.println ("Sprzątaczka porusza sie po ulu");
        movementContext.setStrategy(new RandomMovement());
        return new Point(spawnX, spawnY);
    }

    private void zasoby(Board board){
        Hive ul = board.getHive();
        Simulation.Model.Agents.Queen queen = board.getQueen();
        //Queen queen = board.getQueen;
        //karmienie krolowej
        if(queen != null && queen.getEnergy() < 40.0f){
            if(ul.getHoneyAmount() > 0){
                ul.setHoneyAmount(ul.getHoneyAmount() - 1);
                queen.receiveFood();
            }
        }

        //same jedza
        if(this.getEnergy() < SimulationConfig.ENERGY_THRESHOLD_RETURN){
            if(ul.getHoneyAmount() > 0){
                ul.setHoneyAmount(ul.getHoneyAmount() - 1);
                this.setEnergy(SimulationConfig.ENERGY_FULL);
                System.out.println("Magazynierka " + ID + "zjadla, energia 100%");
            } else {
                System.out.println("Magazynierka " + ID + " glodna, nie zjadla");
            }
        }

        //wytwarzanie jedzenia/miodu
        if(ul.getPollenAmount() > 0){
            ul.setPollenAmount(ul.getPollenAmount() - 1);
            ul.setHoneyAmount(ul.getHoneyAmount() + 1);
        }

    }

    public AgentContext getMovementContext(){
        return this.movementContext;
    }
}
