package Simulation.Model;
public class Hive {
    private int pollenAmount;
    private int honeyAmount;

    public Hive() {
        pollenAmount = 0;
        honeyAmount = SimulationConfig.INITIAL_HIVE_FOOD;
    }

    public synchronized int consumeFood(int amountNeeded) {
        if (honeyAmount <= 0) return 0;

        int toConsume = Math.min(amountNeeded, honeyAmount);
        honeyAmount -= toConsume;
        return toConsume;
    }
    public synchronized void depositPollen(int amount) {
        if (amount > 0) {
            this.pollenAmount += amount;
        }
        System.out.println(amount+ " pollen stashed in hive. Current stash: "+ this.pollenAmount);
    }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int pollenAmount) { this.pollenAmount = pollenAmount; }

    public int getHoneyAmount() { return honeyAmount; }
    public void setHoneyAmount(int honeyAmount) { this.honeyAmount = honeyAmount; }
}