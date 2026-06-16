package Simulation.View;

import Simulation.Logger.Logger;
import Simulation.Model.Agents.*;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Objects;

public class GridBoard extends Canvas {
    private final int TILE_SIZE = 30;

    // Images
    private Image flowerSprite;
    private Image beeSprite;
    private Image BoardView;
    private Image queenSprite;
    private Image larvaSprite;
    private Image storerSprite;

    /**
     * Constructs a new GridBoard and adjusts the canvas sizes according to the simulation dimensions.
     * @param gridWidth number of columns on the board
     * @param gridHeight number of rows on the board
     */
    public GridBoard(int gridWidth, int gridHeight) {
        setWidth(gridWidth * TILE_SIZE);
        setHeight(gridHeight * TILE_SIZE);
        loadSprites();
    }

    /**
     * Loads graphical image files into memory from the application resources folder.
     */
    private void loadSprites() {
        try {
            BoardView = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BoardView.png")));
            beeSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Bee.png")));
            queenSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BeeQueen.png")));
            storerSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BeeStorer.png")));
            larvaSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Larvae.png")));
            flowerSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Flower.png")));
        } catch (Exception e) {
            Logger.log("A critical error occurred while loading images.");
            System.err.println("A critical error occurred while loading images.");
        }
    }

    /**
     * Clears the current canvas frame and performs a full redraw of the background matrix, environmental flowers, and active bee agents
     * @param board the current state data source of the simulation board
     */
    public void render(Board board) {
        GraphicsContext gc = getGraphicsContext2D();

        // Clear the frame
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Draw the background
        if (BoardView != null) {
            gc.drawImage(BoardView, 0, 0, getWidth(), getHeight());
        }

        // Loop through the grid and draw the agents
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                // Draw flowers if any
                Cell cell = board.getCell(x, y);
                if (cell != null && cell.hasFlower()) {
                    gc.drawImage(flowerSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                Bee bee = board.getAgentAt(x, y);
                if(bee == null) continue;
                switch (bee) {
                    case Forager _ -> gc.drawImage(beeSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    case Storer _ -> gc.drawImage(storerSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    case Queen _ -> gc.drawImage(queenSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    case Larva _ -> gc.drawImage(larvaSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    default -> {
                        // Fallback yellow oval
                        gc.setFill(javafx.scene.paint.Color.YELLOW);
                        gc.fillOval(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }
}
