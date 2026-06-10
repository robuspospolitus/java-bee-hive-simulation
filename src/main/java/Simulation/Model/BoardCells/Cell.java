package Simulation.Model.BoardCells;
import Simulation.Model.Agents.Bee;

public class Cell {
    private int x, y;
    private int pollenAmount, regenerationRate, maxPollenAmount;
    private Bee agent;
    private CellType type;
    private boolean hasFlower;
    private static double flowerChance;

    public Cell(int x, int y, CellType type) {
        setType(type!=null ? type : CellType.EMPTY);
        Initialize(x, y);
    }

    private void Initialize(int x, int y) {
        regenerationRate = 1;
        maxPollenAmount = 24;
        this.x = x;
        this.y = y;
        agent = null;
        hasFlower = type.equals(CellType.MEADOW) && Math.random() < flowerChance;
        this.pollenAmount = hasFlower ? maxPollenAmount : 0;
    }

    public boolean isEmpty() {
        // Kafelek jest pusty do chodzenia, jeśli nie stoi na nim inna pszczoła
        return agent == null;
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
    public void setType(CellType typ) {this.type = typ;}

    public static void setFlowerChance(double Chance){flowerChance=Chance; }

    public int getPollenAmount() { return pollenAmount; }
    public void setPollenAmount(int amount) { pollenAmount = amount; }

    public int getMaxPollenAmount() { return maxPollenAmount; }
    public void setMaxPollenAmount(int max) { this.maxPollenAmount = max; }

    public Bee getAgent() { return agent; }
    public void setAgent(Bee agent) { this.agent = agent; }

}
