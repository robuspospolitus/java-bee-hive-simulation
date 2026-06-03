package Simulation.Model;

import Simulation.Model.BoardCells.Cell;

public class Board {

    private Cell[][] grid = new Cell[50][50];

    public Board() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                this.grid[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell[][] getGrid() {
        return this.grid;
    }
}