package Simulation.Model.Agents;

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
        System.out.println ("Storer "+ID+" moves around the hive");
        movementContext.setStrategy(new RandomMovement());
        return new Point(getBeePosition().x, getBeePosition().y);
    }

    private void interact(Board board){
        Hive ul = board.getHive();
        Queen queen = board.getQueen();

        // feeding the queen
        if(queen != null && queen.getEnergy() < 40.0f && ul.getHoneyAmount() > 0){
            feedTheQueen(ul, queen);
            return;
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

    // Private methods

    private void feedTheQueen(Hive ul, Queen queen) {
        ul.setHoneyAmount(ul.getHoneyAmount() - 1);
        queen.receiveFood();
        System.out.println("Storer " + ID + " fed the Queen.");
    }

    private void eat(Hive ul) {
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
            System.out.println("Storer " + ID + " ate " + toConsume + " honey, energy: " + energy);
        } else {
            System.out.println("Storer " + ID + " is hungry, did not eat");
        }
    }
    private void makeHoney(Hive ul) {
        ul.setPollenAmount(ul.getPollenAmount() - 1);
        ul.setHoneyAmount(ul.getHoneyAmount() + SimulationConfig.HONEY_FROM_POLLEN);
        System.out.println("Storer " + ID + " processed pollen into honey.");
    }
    private void feedLarva(Board board) {
        Hive ul = board.getHive();
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Cell cell = board.getCell(x, y);
                if (cell != null && cell.getAgent() instanceof Larva larva) {
                    ul.setHoneyAmount(ul.getHoneyAmount() - 1);
                    larva.beFed();
                    System.out.println("Storer " + ID + " fed larva " + larva.getID() + " in position  (" + x + "," + y + ")");
                    return;
                }
            }
        }
    }
}
