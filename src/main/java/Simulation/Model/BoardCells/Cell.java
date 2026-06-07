package Simulation.Model.BoardCells;
import Simulation.Model.Agents.Bee;

public class Cell {
    private int x, y;
    private int pollenAmount, regenerationRate;
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
        flowerChance = 0.5;
        hasFlower = type.equals(CellType.MEADOW) && Math.random() < flowerChance;
        this.x = x;
        this.y = y;
        agent = null;
    }

    public boolean isEmpty() {
        if(!hasFlower && agent == null) return true;
        return false;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public CellType getType() { return type; }
    public void setType() { this.type = type; }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int amount) { pollenAmount = amount; }

    public Bee getAgent() { return agent; }
    public void setAgent(Bee agent) { this.agent = agent; }

    public void regeneratePollen() { pollenAmount += regenerationRate; }

}
