package Simulation.Model;


import Simulation.Model.Agents.Bee;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;
import java.awt.*;

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];

        // Initialize every single coordinate with a blank Cell object
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(x >= 14 && x <= 16 && y <= 2) {
                    grid[x][y] = new Cell(x, y, CellType.EMPTY);
                } else {
                    grid[x][y] = x<12 ? new Cell(x, y, CellType.HIVE) :
                            x==12 ? new Cell(x, y, CellType.OBSTACLE) :
                                    new Cell(x, y, CellType.MEADOW);
                }
            }
        }
    }

    // Regenerate pollen on Cells
    public void regenerateEnvironment() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].regeneratePollen();
            }
        }
    }

    // --- Core Movement Logic ---

    // Checks if a coordinate is actually on the map, and if no other bee is there
    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if (grid[x][y].getType() == CellType.OBSTACLE) return false;

        return grid[x][y].isEmpty();
    }

    // Handles the actual teleportation of a bee from old cell to new cell
    public void moveAgent(Bee bee, Point oldPos, Point newPos) {
        // Clear the old cell if the bee was already on the board
        if (oldPos != null) {
            grid[oldPos.x][oldPos.y].setAgent(null);
        }
        // Set the bee into the new cell
        if (newPos != null) {
            grid[newPos.x][newPos.y].setAgent(bee);
        }
    }

    // --- View & Engine Helpers ---

    // Allows SimulationEngine to paint "Hive" or "Meadow" zones onto the grid
//    public void setZoneType(int startX, int endX, int startY, int endY, CellType type) {
//        for (int x = startX; x <= endX; x++) {
//            for (int y = startY; y <= endY; y++) {
//                if (isValidCoordinate(x, y)) {
//                    grid[x][y].setType(CellType.EMPTY);
//                }
//            }
//        }
//    }

    public Cell getCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public Bee getAgentAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y].getAgent();
        }
        return null;
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getGrid() {
        return this.grid;
    }
}