package Simulation.View;

import Simulation.Model.Agents.*;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Objects;

public class GridBoard extends Canvas {

    private final int TILE_SIZE = 32;

    // 1. Declare your image cache variables here
    private Image flowerSprite;
    private Image beeSprite;
    private Image BoardView;
    private Image queenSprite;
    private Image larvaSprite;
    private Image storerSprite;

    public GridBoard(int gridWidth, int gridHeight) {
        setWidth(gridWidth * TILE_SIZE);
        setHeight(gridHeight * TILE_SIZE);

        // 2. Load the images immediately when the board is created
        loadSprites();
    }

    private void loadSprites() {
        try {
            // getClass().getResource() looks inside the src/main/resources folder
            BoardView = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BoardView.png")));
            beeSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Bee.png")));
            queenSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BeeQueen.png")));
            storerSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/BeeStorer.png")));
            larvaSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Larvae.png")));
            flowerSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Flower.png")));

            // You can add more here later: queenSprite, hiveSprite, etc.
        } catch (NullPointerException e) {
            System.err.println("Could not find image files! Check your resources folder.");
        }
    }

    public void render(Board board) {
        GraphicsContext gc = getGraphicsContext2D();

        // 3. Clear the frame
        gc.clearRect(0, 0, getWidth(), getHeight());

        // 4. Draw the background FIRST (so it sits behind the bees)
        // This stretches the background image to fill the entire canvas
        if (BoardView != null) {
            gc.drawImage(BoardView, 0, 0, getWidth(), getHeight());
        }

        // 5. Loop through the grid and draw the agents
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                // Draw flowers if any
                Cell cell = board.getCell(x, y);
                if (cell != null && cell.hasFlower() && flowerSprite != null) {
                    gc.drawImage(flowerSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                Bee bee = board.getAgentAt(x, y);
                if (bee != null) {
                    if (bee instanceof Forager && beeSprite != null) {

                        gc.drawImage(beeSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);}
                        else if (bee instanceof Storer && beeSprite != null) {
                            gc.drawImage(storerSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        } else if (bee instanceof Queen && beeSprite != null) {
                            gc.drawImage(queenSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE); }
                        else if (bee instanceof Larva && beeSprite != null) {
                        // Draw the sprite instead of the oval!
                        gc.drawImage(larvaSprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);}
                        else {
                        // Fallback if it's a bee we don't have a sprite for yet
                            gc.setFill(javafx.scene.paint.Color.YELLOW);
                            gc.fillOval(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }
}
