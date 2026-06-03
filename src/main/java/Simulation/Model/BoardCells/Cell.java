package Simulation.Model.BoardCells;
import Simulation.Model.Agents.Bee;

public class Cell {
    private int x, y, pollenAmount, regenerationRate;
    private Bee agent;
    private String type;
    private boolean hasFlower;
    private double flowerChance;

    public Cell(int x, int y) {
        Initialize(x, y);
        type = "grass";

    }
    public Cell(int x, int y, String type) {
        Initialize(x, y);
        this.type = type;
    }

    private void Initialize(int x, int y) {
        regenerationRate = 1;
        flowerChance = 0.5;
        hasFlower = type.equals("grass") && Math.random() < flowerChance;
        this.x = x;
        this.y = y;
        agent = null;
    }

    public boolean isEmpty() {
        if(!hasFlower && agent == null) return true;
        return false;
    }

    public String getType() { return type; }
    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int amount) { pollenAmount = amount; }
    public void regeneratePollen() { pollenAmount += regenerationRate; }
}
