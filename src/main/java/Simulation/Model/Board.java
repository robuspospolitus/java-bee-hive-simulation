package Simulation.Model;

import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Queen;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;
import java.awt.*;

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;
    private final Point hiveEntrance = new Point (15,1);
    private final Point hiveExit = new Point (11,1);

    private Hive hive;
    private Simulation.Model.Agents.Queen queen;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        hive = new Hive();

        // Initialize every single Cell
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if((x==hiveEntrance.x && y==hiveEntrance.y)|| (x==hiveExit.x && y==hiveExit.y)){
                   grid[x][y] = new Cell(x, y, CellType.TELEPORT);
                } else if(x<=16 && x>=14 && y<=2){
                    grid[x][y] = new Cell (x,y, CellType.EMPTY);
                } else {
                    grid[x][y] = x<12 ? new Cell(x, y, CellType.HIVE) :
                            x==12 ? new Cell(x, y, CellType.OBSTACLE) :
                                    new Cell(x, y, CellType.MEADOW);
                }
            }
        }
        setStashCells();
    }

    private void setStashCells() {
        for (int x = 0; x <= 2; x++){
            grid[x][0].setType(CellType.POLLEN_STASH);
            grid[x][1].setType(CellType.HONEY_STASH);
        }
    }

    public Hive getHive() {
        return hive;
    }

    // Regenerate pollen on Cells
    public void regenerateEnvironment() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].regeneratePollen();
            }
        }
    }


    // Checks if a coordinate is actually on the map, and if no other bee is there
    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if (grid[x][y].getType() == CellType.OBSTACLE) return false;
       return grid[x][y].isEmpty();
    }

    // Handles moving bee from old cell to new cell
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

    public Point getTeleportDestination(Point entrance) {
        if (entrance.equals(hiveEntrance)) {
            return hiveExit;
        } else if (entrance.equals(hiveExit)) {
            return hiveEntrance;
        }
        return null;
    }

    public Point getStashDestination(CellType stashType){
        if (stashType==CellType.POLLEN_STASH) {
            return new Point (0,0);
        } else if (stashType==CellType.HONEY_STASH) {
            return new Point (0,1);
        }
        return null;
    }

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

    public Queen getQueen() {
        return queen;
    }

    public void setQueen(Queen queen) {
        this.queen = queen;
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

    public Point getHiveEntrance(){return hiveEntrance;}
    public Point getHiveExit(){return hiveExit;}
}