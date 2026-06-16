package Simulation.Model;

import Simulation.Logger.Logger;

public class Hive {
    private int pollenAmount;
    private int honeyAmount;

    /**
     * Constructs a new Hive instance with zero initial pollen and configures the starting honey stock based on the simulation configurations.
     */
    public Hive() {
        pollenAmount = 0;
        honeyAmount = SimulationConfig.INITIAL_HIVE_FOOD;
    }

    /**
     * Consumes a requested amount of honey from the hive storage. Synchronized to prevent over-allocation during concurrent accesses.
     * @param amountNeeded the quantity of honey requested
     * @return the actual amount of honey successfully consumed
     */
    public synchronized int consumeFood(int amountNeeded) {
        if (honeyAmount <= 0) return 0;

        int toConsume = Math.min(amountNeeded, honeyAmount);
        honeyAmount -= toConsume;
        return toConsume;
    }
    /**
     * Deposits collected pollen into the hive storage.
     * @param amount the quantity of pollen to deposit
     * @return the updated total amount of pollen inside the hive
     */
    public synchronized int depositPollen(int amount) {
        if (amount == 0) return 0;
        pollenAmount += amount;
        Logger.log(amount+ " pollen stashed in hive. Current stash: "+ pollenAmount);
        return pollenAmount;
    }

    /**
     * Gets the total amount of pollen currently available in the hive.
     * @return the current pollen count
     */
    public int getPollenAmount() { return pollenAmount; }
    /**
     * Overrides the current amount of pollen in the hive.
     * @param pollenAmount the new pollen count to set
     */
    public void setPollenAmount(int pollenAmount) { this.pollenAmount = pollenAmount; }

    /**
     * Gets the total amount of honey currently stored in the hive.
     * @return the current honey count
     */
    public int getHoneyAmount() { return honeyAmount; }
    /**
     * Overrides the current amount of honey in the hive.
     * @param honeyAmount the new honey count to set
     */
    public void setHoneyAmount(int honeyAmount) { this.honeyAmount = honeyAmount; }
}