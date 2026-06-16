package Simulation.Model.BoardCells;
import Simulation.Model.Agents.Bee;

public class Cell {
    private int x, y;
    private int pollenAmount, regenerationRate, maxPollenAmount;
    private Bee agent;
    private CellType type;
    private boolean hasFlower;
    private static double flowerChance;

    /**
     * Creates a new grid cell with a specific type and coordinates.
     * @param x the X coordinate on the board
     * @param y the Y coordinate on the board
     * @param type the initial environmental type of the cell
     */
    public Cell(int x, int y, CellType type) {
        setType(type!=null ? type : CellType.EMPTY);
        regenerationRate = 1;
        maxPollenAmount = 24;
        this.x = x;
        this.y = y;
        agent = null;
        hasFlower = type.equals(CellType.MEADOW) && Math.random() < flowerChance;
        pollenAmount = hasFlower ? maxPollenAmount : 0;
    }

    /**
     * Checks if the cell is currently unoccupied by any bee agent.
     * @return true if there is no agent on the cell, false otherwise
     */
    public boolean isEmpty() {
        return agent == null;
    }

    /**
     * Takes a requested amount of pollen from the flower, if available.
     * @param amount the amount of pollen to collect
     * @return the actual amount of pollen successfully collected
     */
    public int takePollen(int amount) {
        if (!hasFlower) return 0;
        int available = Math.min(amount, pollenAmount);
        pollenAmount -= available;
        return available;
    }

    /**
     * Gradually restores pollen over time up to the maximum capacity.
     */
    public void regeneratePollen() {
        if (hasFlower && pollenAmount < maxPollenAmount) {
            pollenAmount = Math.min(maxPollenAmount, pollenAmount + regenerationRate);
        }
    }

    /**
     * Gets the X coordinate of the cell.
     * @return the X coordinate
     */
    public int getX() { return x; }
    /**
     * Gets the Y coordinate of the cell.
     * @return the Y coordinate
     */
    public int getY() { return y; }

    /**
     * Checks if the cell contains a flower.
     * @return true if the cell has a flower, false otherwise
     */
    public boolean hasFlower() { return hasFlower; }

    /**Sets if a cell contains a flower */
    public void setFlower(boolean hasFlower){this.hasFlower=hasFlower;}

    /**
     * Gets the environmental type of the cell.
     * @return the current cell type
     */
    public CellType getType() { return type; }
    /**
     * Sets the environmental type of the cell.
     * @param typ the new cell type to assign
     */
    public void setType(CellType typ) {this.type = typ;}

    /**
     * Sets the global spawning chance for flowers across the simulation.
     * @param Chance the probability value between zero and one
     */
    public static void setFlowerChance(double Chance){flowerChance=Chance; }

    /**
     * Gets the current amount of pollen available in the cell.
     * @return the pollen count
     */
    public int getPollenAmount() { return pollenAmount; }
    /**
     * Sets the current amount of pollen in the cell.
     * @param amount the new pollen amount
     */
    public void setPollenAmount(int amount) { pollenAmount = amount; }

    /**
     * Gets the maximum pollen capacity of the cell.
     * @return the maximum pollen amount
     */
    public int getMaxPollenAmount() { return maxPollenAmount; }
    /**
     * Sets the maximum pollen capacity of the cell.
     * @param max the new maximum pollen limit
     */
    public void setMaxPollenAmount(int max) { this.maxPollenAmount = max; }

    /**
     * Gets the bee agent currently occupying the cell.
     * @return the bee agent instance, or null if empty
     */
    public Bee getAgent() { return agent; }
    /**
     * Assigns a bee agent to occupy this cell.
     * @param agent the bee agent to place on the cell
     */
    public void setAgent(Bee agent) { this.agent = agent; }
}
