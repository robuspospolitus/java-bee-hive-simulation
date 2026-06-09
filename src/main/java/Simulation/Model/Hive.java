package Simulation.Model;
public class Hive {
    private int pollenAmount;
    private int honeyAmount;

    public Hive() {
        this.pollenAmount = 0;
        this.honeyAmount = SimulationConfig.INITIAL_HIVE_FOOD;
    }

    public synchronized int consumeFood(int amountNeeded) {
        if (this.honeyAmount <= 0) {
            return 0;
        }
        if (this.honeyAmount >= amountNeeded) {
            this.honeyAmount -= amountNeeded;
            return amountNeeded;
        } else {
            int availableFood = this.honeyAmount;
            this.honeyAmount = 0;
            return availableFood;
        }
    }
    public synchronized void depositPollen(int amount) {
        if (amount > 0) {
            this.pollenAmount += amount;
        }
    }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int pollenAmount) { this.pollenAmount = pollenAmount; }

    public int getHoneyAmount() { return honeyAmount; }
    public void setHoneyAmount(int honeyAmount) { this.honeyAmount = honeyAmount; }
}