package Simulation.Model.BoardCells;
import Simulation.Model.Agents.Bee;

public class Cell {
    private int x, y;
    private int pollenAmount, regenerationRate, maxPollenAmount;
    private Bee agent;
    private CellType type;
    private boolean hasFlower;
    private double flowerChance;

    public Cell(int x, int y) {
        this.type = CellType.EMPTY;
        Initialize(x, y);
    }
    public Cell(int x, int y, CellType type) {
        this.type = type;
        Initialize(x, y);
    }

    private void Initialize(int x, int y) {
        regenerationRate = 1;
        maxPollenAmount = 24;
        flowerChance = 0.3;
        hasFlower = type.equals(CellType.MEADOW) && Math.random() < flowerChance;
        this.x = x;
        this.y = y;
        this.pollenAmount = hasFlower ? maxPollenAmount : 0;
        agent = null;
    }

    public boolean isEmpty() {
        return !hasFlower && agent == null;
    }

    public int takePollen(int amount) {
        if (!hasFlower) return 0;
        int available = Math.min(amount, pollenAmount);
        pollenAmount -= available;
        return available;
    }

    public void regeneratePollen() {
        if (hasFlower && pollenAmount < maxPollenAmount) {
            pollenAmount = Math.min(maxPollenAmount, pollenAmount + regenerationRate);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean hasFlower() { return hasFlower; }

    public CellType getType() { return type; }
    public void setType(CellType typ) { this.type = typ; }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int amount) { pollenAmount = amount; }

    public int getMaxPollenAmount() { return maxPollenAmount; }
    public void setMaxPollenAmount(int max) { this.maxPollenAmount = max; }

    public Bee getAgent() { return agent; }
    public void setAgent(Bee agent) { this.agent = agent; }

}
