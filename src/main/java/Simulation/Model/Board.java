package Simulation.Model;

import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Queen;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;
import java.awt.*;

/**
 * Represents the main simulation board managing the grid layout, environmental cells,
 * and spatial synchronization of bee agents.
 */
public class Board {
    private int width;
    private int height;
    private Cell[][] grid;
    private final Point hiveEntrance = new Point (15,1);
    private final Point hiveExit = new Point (11,1);
    private Hive hive;
    private Simulation.Model.Agents.Queen queen;

    /**
     * Initializes a new Board with specified dimensions, constructs the grid cells,
     * maps specific zones like the hive, obstacle boundaries, and meadows, and configures resource stashes.
     * @param width the horizontal size of the board grid
     * @param height the vertical size of the board grid
     */
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

    /**
     * Configures dedicated storage locations for pollen and honey within the grid.
     */
    private void setStashCells() {
        for (int x = 0; x <= 2; x++){
            grid[x][0].setType(CellType.POLLEN_STASH);
            grid[x][1].setType(CellType.HONEY_STASH);
        }
    }

    /**
     * Retrieves the core Hive object associated with this simulation board.
     * @return the hive instance managing total global resources
     */
    public Hive getHive() {
        return hive;
    }

    /**
     * Iterates through the entire grid and triggers pollen restoration for available flower cells.
     */
    public void regenerateEnvironment() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].regeneratePollen();
            }
        }
    }


    /**
     * Checks if a target coordinate is within valid bounds, not blocked by an obstacle, and currently unoccupied.
     * @param x target X coordinate
     * @param y target Y coordinate
     * @return true if the cell is valid and available for movement, false otherwise
     */
    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if (grid[x][y].getType() == CellType.OBSTACLE) return false;
       return grid[x][y].isEmpty();
    }

    /**
     * Handles the spatial transfer of an agent from an old cell position to a new destination on the grid.
     * @param bee the bee agent executing the transition
     * @param oldPos the previous position coordinates, or null if entering the board
     * @param newPos the target destination coordinates, or null if leaving the board
     */
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

    /**
     * Determines the opposite endpoint mapping for portal locations like hive entrances or exits.
     * @param entrance the origin portal point
     * @return the linked destination point, or null if the coordinate is not a recognized portal
     */
    public Point getTeleportDestination(Point entrance) {
        if (entrance.equals(hiveEntrance)) {
            return hiveExit;
        } else if (entrance.equals(hiveExit)) {
            return hiveEntrance;
        }
        return null;
    }

    /**
     * Gives extraction points for resource stashes depending on the storage type.
     * @param stashType resource storage type
     * @return the designated default coordinate point for that stash category
     */
    public Point getStashDestination(CellType stashType){
        if (stashType==CellType.POLLEN_STASH) {
            return new Point (0,0);
        } else if (stashType==CellType.HONEY_STASH) {
            return new Point (0,1);
        }
        return null;
    }

    /**
     * Gets a grid cell at the given coordinates after verifying boundary validity.
     * @param x cell X coordinate
     * @param y cell Y coordinate
     * @return the Cell object, or null if coordinates are out of bounds
     */
    public Cell getCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    /**
     * Identifies any agent currently positioned at the specified coordinates.
     * @param x search X coordinate
     * @param y search Y coordinate
     * @return the occupying Bee agent instance, or null if the coordinate is empty or invalid
     */
    public Bee getAgentAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y].getAgent();
        }
        return null;
    }

    /**
     * Gets the single Queen agent tracked by this board instance
     * @return the queen instance
     */
    public Queen getQueen() {
        return queen;
    }

    /**
     * Registers the new queen bee instance
     * @param queen the queen agent object
     */
    public void setQueen(Queen queen) {
        this.queen = queen;
    }

    /**
     * Checks if a point falls within the legal boundaries of the board configuration grid.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Gets the current width of the simulation board.
     * @return total grid width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the current height of the simulation board.
     * @return total grid height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the two-dimensional cell grid representation.
     * @return the raw matrix array of grid cells
     */
    public Cell[][] getGrid() {
        return this.grid;
    }

    /**
     * Gets hive entrance
     * @return hive entrance location
     */
    public Point getHiveEntrance(){return hiveEntrance;}
    /**
     * Gets hive exit
     * @return hive exit location
     */
    public Point getHiveExit(){return hiveExit;}


}