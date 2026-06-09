package Simulation.Model;
public class Hive {
    private int pollenAmount;
    private int honeyAmount;
    private int foodAmount;

    public Hive() {
        this.pollenAmount = 0;
        this.foodAmount = SimulationConfig.INITIAL_HIVE_FOOD;
    }

    public synchronized int consumeFood(int amountNeeded) {
        if (this.foodAmount <= 0) {
            return 0;
        }
        if (this.foodAmount >= amountNeeded) {
            this.foodAmount -= amountNeeded;
            return amountNeeded;
        } else {
            int availableFood = this.foodAmount;
            this.foodAmount = 0;
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

    public int getFoodAmount() { return foodAmount; }
    public void setFoodAmount(int foodAmount) { this.foodAmount = foodAmount; }
}