package Simulation.Model;
public class Hive {
    private int pollenAmount;
    private int honeyAmount;
    private int foodAmount;

    public Hive() {
        this.pollenAmount = 0;
        this.honeyAmount = 0;
        this.foodAmount = 20; // cos na start
    }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int pollenAmount) { this.pollenAmount = pollenAmount; }

    public int getHoneyAmount() { return honeyAmount; }
    public void setHoneyAmount(int honeyAmount) { this.honeyAmount = honeyAmount; }

    public int getFoodAmount() { return foodAmount; }
    public void setFoodAmount(int foodAmount) { this.foodAmount = foodAmount; }
}