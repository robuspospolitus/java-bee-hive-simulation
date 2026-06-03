package Simulation.Model;
import Simulation.Model.Agents.Bee;
import java.awt.Point;


import Simulation.Model.BoardCells.Cell;

public class Board {
    private final int width;
    private final int height;

    private final Cell[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
    }


}
