package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.BoardCells.Cell;

import java.awt.*;

public class Storer extends Bee{
    //int spawnX;
    //int spawnY;
    //Point spawnPosition;

    public Storer (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY);
        //this.spawnPosition = new Point(spawnX, spawnY);
        this.movementContext = new AgentContext("Storer" + ID, new RandomMovement(), new Point (spawnX, spawnY));
        this.setEnergy(100.0f);
    }

    @Override
    public void move(Board board){
        findDestination(board);
        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove();
        Point newPos = movementContext.getPosition();
        board.moveAgent(this, oldPos, newPos);
        System.out.println("Magazynierka " + ID + " poruszyla sie");
    }

    protected Point findDestination(Board board){
        System.out.println ("Sprzątaczka porusza sie po ulu");
        movementContext.setStrategy(new RandomMovement());
        return movementContext.getPosition();
    }

    @Override
    protected void interactWithEnvironment(Board board){
        Hive ul = board.getHive();
        Simulation.Model.Agents.Queen queen = board.getQueen();

        //karmienie krolowej
        if(queen != null && queen.getEnergy() < 40.0f){
            if(ul.getFoodAmount() > 0){
                ul.setFoodAmount(ul.getFoodAmount() - 1);
                queen.receiveFood();
            }
        }

        //storery jedza
        if(this.getEnergy() < 20.0f){
            if(ul.getFoodAmount() > 0){
                ul.setFoodAmount(ul.getFoodAmount() - 1);
                this.setEnergy(100.0f);
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


        //karmienie larw
        /*
        Point currentPos = this.getBeePosition();
        Cell currentCell = board.getCell(currentPos.x, currentPos.y);

        if (currentCell != null && currentCell.hasLarva()) {
            Larva larva = currentCell.getLarva();

            if (larva != null && larva.getEnergy() < 50.0f) {
                if (ul.getFoodAmount() > 0) {
                    ul.setFoodAmount(ul.getFoodAmount() - 1);
                    larva.feed();
                    System.out.println(" Magazynierka " + getID() + " nakarmiła głodną Larwę " + larva.getID());
                } else {
                    System.out.println(" Magazynierka " + getID() + " chce nakarmić Larwę " + larva.getID() + ", ale brak jedzenia");
                }
            }
        }
        */
    }

    public AgentContext getMovementContext(){
        return this.movementContext;
    }
}
