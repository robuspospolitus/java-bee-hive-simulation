package Simulation.Model.Agents;
import Simulation.Logger.Logger;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public class Queen extends Bee{
    private int eggLayCooldown = 0; // egg laying turn counter

    /**
     * Creates a new Queen bee with a zero initial egg laying cooldown.
     * @param ID unique identifier of the queen
     * @param age initial age of the queen
     * @param spawnX starting X coordinate
     * @param spawnY starting Y coordinate
     */
    public Queen (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY, null, "Queen");
    }

    /**
     * Updates the queen's state by aging, consuming energy, and reducing the egg cooldown.
     * @param board the simulation board
     */
    @Override
    public void move(Board board){
        age++;
        burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_QUEEN);

        if(eggLayCooldown > 0){
            eggLayCooldown--;
        }
    }

    /**
     * Overrides method of checking age.The queen is immune to aging limits.
     * @return always false
     */
    @Override
    public boolean isTooOld() {
        return false; // The Queen is never "too old"
    }
    /**
     * Returns the initial spawn position since the queen does not move.
     * @param board the simulation board
     * @return the stationary spawn coordinates
     */
    @Override
    Point findDestination(Board board){ return spawnPosition; }
    @Override
    public AgentContext getMovementContext(){ return movementContext; }
    @Override
    public Point getBeePosition() { return spawnPosition; }

    /**
     * Checks if the queen is ready to lay an egg based on cooldown and energy.
     * @return true if cooldown is zero and energy is above thirty, false otherwise
     */
    public boolean canLayEgg(){
        return eggLayCooldown == 0 && getEnergy() > 30; // how much energy she needs to lay an egg
    }
    /**
     * Resets the egg laying cooldown counter to the configured limit.
     */
    public void resetEggCooldown(){
        eggLayCooldown = SimulationConfig.EGG_LAY_COOLDOWN;
    }
    /**
     * Replenishes the queen's energy level back to full capacity.
     */
    public void receiveFood(){
        setEnergy(SimulationConfig.ENERGY_FULL);
    }
    /**
     * Lays an egg and creates a new Larva agent at the specified safe coordinates.
     * @param newId unique identifier for the new larva
     * @param safeX the X coordinate where the egg is laid
     * @param safeY the Y coordinate where the egg is laid
     * @return the newly created Larva object
     */
    public Larva layEggs(int newId, int safeX, int safeY){
        Logger.log("The eggs were laid by the queen on " + safeX + ", " + safeY);
        return new Larva(newId, safeX, safeY);
    }
}
