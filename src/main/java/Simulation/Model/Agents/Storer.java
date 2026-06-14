package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.SimulationConfig;

import java.awt.*;

public class Storer extends Bee{
    public Storer (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY, new RandomMovement(), "Magazynierka");
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
            boolean dodged = false;

            // Look at tiles surrounding the bee
            for (int xOffset = -1; xOffset <= 1 && !dodged; xOffset++) {
                for (int yOffset = -1; yOffset <= 1 && !dodged; yOffset++) {

                    int dodgeX = oldPos.x + xOffset;
                    int dodgeY = oldPos.y + yOffset;

                    // If an empty tile nearby, go
                    if (board.isValidMove(dodgeX, dodgeY)) {
                        Point dodgePos = new Point(dodgeX, dodgeY);
                        movementContext.setPosition(dodgePos);
                        board.moveAgent(this, oldPos, dodgePos);
                        dodged = true;
                    }
                }
            }

            // If surrounded on all 8 sides, wait
            if (!dodged) {
                movementContext.setPosition(oldPos);
            }
        }

        age++;
        burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_STORER);
        zasoby(board);
    }

    protected Point findDestination(Board board){
        System.out.println ("Sprzątaczka porusza sie po ulu");
        movementContext.setStrategy(new RandomMovement());
        return new Point(getBeePosition().x, getBeePosition().y);
    }

    private void zasoby(Board board){
        Hive ul = board.getHive();
        Queen queen = board.getQueen();

        // feeding the queen
        if(queen != null && queen.getEnergy() < 40.0f){
            if(ul.getHoneyAmount() > 0){
                ul.setHoneyAmount(ul.getHoneyAmount() - 1);
                queen.receiveFood();
            }
        }

        // eat
        if(energy < SimulationConfig.ENERGY_THRESHOLD_RETURN){
            int foodAvailable = ul.getHoneyAmount();
            if(foodAvailable > 0) {
                float energyNeeded = SimulationConfig.ENERGY_FULL - energy;
                int honeyNeeded = (int) Math.ceil(energyNeeded / SimulationConfig.ENERGY_PER_HONEY);
                int toConsume = Math.min(honeyNeeded, foodAvailable);

                ul.setHoneyAmount(foodAvailable - toConsume);
                energy += toConsume * SimulationConfig.ENERGY_PER_HONEY;
                if (energy > SimulationConfig.ENERGY_FULL) {
                    energy = SimulationConfig.ENERGY_FULL;
                }
                System.out.println("Magazynierka " + ID + " zjadła " + toConsume + " miodu, energia: " + energy);
            } else {
                System.out.println("Magazynierka " + ID + " glodna, nie zjadla");
            }
        }

        // production of honey
        if(ul.getPollenAmount() > 0){
            ul.setPollenAmount(ul.getPollenAmount() - 1);
            ul.setHoneyAmount(ul.getHoneyAmount() + SimulationConfig.HONEY_FROM_POLLEN);
        }

    }

    public boolean isReadyToEvolve() {
        return age >= SimulationConfig.TICKS_TO_EVOLVE;
    }

    public AgentContext getMovementContext(){
        return movementContext;
    }
}
