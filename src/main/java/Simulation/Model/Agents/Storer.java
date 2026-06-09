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

        if (board.isValidMove(newPos.x, newPos.y)) {
            board.moveAgent(this, oldPos, newPos);
        } else {
            // 2. TRAFFIC JAM! Try to sidestep instead of giving up!
            boolean dodged = false;

            // Look at the 8 adjacent tiles surrounding the bee
            for (int xOffset = -1; xOffset <= 1 && !dodged; xOffset++) {
                for (int yOffset = -1; yOffset <= 1 && !dodged; yOffset++) {

                    int dodgeX = oldPos.x + xOffset;
                    int dodgeY = oldPos.y + yOffset;

                    // If we find an empty tile nearby, slide into it!
                    if (board.isValidMove(dodgeX, dodgeY)) {
                        Point dodgePos = new Point(dodgeX, dodgeY);
                        movementContext.setPosition(dodgePos);
                        board.moveAgent(this, oldPos, dodgePos);
                        dodged = true; // Mark as successful to stop the loop
                    }
                }
            }

            // 3. If completely surrounded on all 8 sides, just wait patiently
            if (!dodged) {
                movementContext.setPosition(oldPos);
            }
        }


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

    public boolean isReadyToEvolve() {
        return this.age >= SimulationConfig.TICKS_TO_EVOLVE;
    }

    public AgentContext getMovementContext(){
        return this.movementContext;
    }
}
