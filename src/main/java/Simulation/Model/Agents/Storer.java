package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;

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
        movementContext.performMove();
        Point newPos = movementContext.getPosition();
        board.moveAgent(this, oldPos, newPos);
        System.out.println("Magazynierka " + ID + " poruszyla sie");

        this.age++;
        this.setEnergy(this.getEnergy() - 1);
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
        if(queen.getEnergy() < 40.0f){
            if(ul.getFoodAmount() > 0){
                ul.setFoodAmount(ul.getFoodAmount() - 1);
                queen.receiveFood();
            }
        }

        //same jedza
        if(this.getEnergy() < 20){
            if(ul.getFoodAmount() > 0){
                ul.setFoodAmount(ul.getFoodAmount() - 1);
                this.setEnergy((100));
                System.out.println("Magazynierka " + ID + "zjadla, energia 100%");
            } else {
                System.out.println("Magazynierka " + ID + " glodna, nie zjadla");
            }
        }


        //wytwarzanie jedzenia/miodu
        if(ul.getPollenAmount() >= 2){
            ul.setPollenAmount(ul.getPollenAmount() - 2);
            ul.setHoneyAmount(ul.getHoneyAmount()+ 1);
            ul.setFoodAmount(ul.getFoodAmount() + 1);
        }

    }

    public AgentContext getMovementContext(){
        return this.movementContext;
    }
}
