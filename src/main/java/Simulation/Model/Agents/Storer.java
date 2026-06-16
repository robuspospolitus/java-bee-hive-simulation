package Simulation.Model.Agents;

import Simulation.Logger.Logger;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.Hive;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.SimulationConfig;

import java.awt.*;

public class Storer extends Bee{
    public Storer (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY, new RandomMovement(), "Storer");
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
        interact(board);
    }

    protected Point findDestination(Board board){
        Logger.log ("Storer "+ID+" moves around the hive");
        movementContext.setStrategy(new RandomMovement());
        return new Point(getBeePosition().x, getBeePosition().y);
    }

    private void interact(Board board){
        Hive ul = board.getHive();
        Queen queen = board.getQueen();

        // feeding the queen
        if(queen != null && queen.getEnergy() < 40.0f){
            if (ul.consumeFood(1) > 0) {
                queen.receiveFood();
                Logger.log("Storer " + ID + " fed the Queen.");
                return;
            }
        }
        // eat
        if(energy < SimulationConfig.ENERGY_THRESHOLD_RETURN) {
            eat(ul);
            return;
        }
        // production of honey
        if(ul.getPollenAmount() > 0){
            makeHoney(ul);
            return;
        }
        // feed one larva at a time
        if (ul.getHoneyAmount() > 0) {
            feedLarva(board);
        }
    }

    public boolean isReadyToEvolve() {
        return age >= SimulationConfig.TICKS_TO_EVOLVE;
    }

    public AgentContext getMovementContext(){
        return movementContext;
    }

    private void eat(Hive ul) {
        float energyNeeded = SimulationConfig.ENERGY_FULL - energy;
        int honeyNeeded = (int) Math.ceil(energyNeeded / SimulationConfig.ENERGY_PER_HONEY);
        int toConsume = ul.consumeFood(honeyNeeded);

        if(toConsume > 0) {
            energy += toConsume * SimulationConfig.ENERGY_PER_HONEY;
            if (energy > SimulationConfig.ENERGY_FULL) {
                energy = SimulationConfig.ENERGY_FULL;
            }
            Logger.log("Storer " + ID + " ate " + toConsume + " honey, energy: " + energy);
        } else {
            Logger.log("Storer " + ID + " is hungry, did not eat");
        }
    }
    private void makeHoney(Hive ul) {
        ul.setPollenAmount(ul.getPollenAmount() - 1);
        ul.setHoneyAmount(ul.getHoneyAmount() + SimulationConfig.HONEY_FROM_POLLEN);
        Logger.log("Storer " + ID + " processed pollen into honey.");
    }
    private void feedLarva(Board board) {
        Hive ul = board.getHive();
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Cell cell = board.getCell(x, y);
                if (cell != null && cell.getAgent() instanceof Larva larva) {
                    if (ul.consumeFood(1) > 0) {
                        larva.beFed();
                        Logger.log("Storer " + ID + " fed larva " + larva.getID() + " in position  (" + x + ", " + y + ")");
                    } else  Logger.log("Storer " + ID + " wanted to feed larva, but there is no honey in the hive.");
                    return;
                }
            }
        }
    }
}
