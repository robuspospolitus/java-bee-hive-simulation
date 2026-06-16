package Simulation.Controller;

import Simulation.Model.SimulationEngine;
import Simulation.View.GridBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Controls the execution flow of the simulation, binding the core business logic
 * inside the engine with the visual rendering layer using a JavaFX loop.
 */
public class SimulationController {
    private SimulationEngine engine;
    private final GridBoard gridBoard;
    private Timeline gameLoop;
    private Runnable onTickCallback;

    /**
     * Constructs a SimulationController and associates it with the drawing canvas.
     * @param gridBoard the canvas display component used for rendering the board state
     */
    public SimulationController(GridBoard gridBoard) {
        this.gridBoard = gridBoard;
    }

    /**
     * Initializes a fresh simulation engine state and triggers the main animation timer loop.
     * @param storers the initial number of storer bees to create
     * @param foragers the initial number of forager bees to create
     * @param flowerChance the resource spawn probability factor
     */
    public void start(int storers, int foragers, double flowerChance) {
        this.engine = new SimulationEngine(storers, foragers, flowerChance);
        startLoop();
    }

    /**
     * Configures, schedules, and starts the asynchronous JavaFX Timeline loop responsible for step execution and UI synchronization.
     */
    private void startLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        gameLoop = new Timeline(new KeyFrame(Duration.millis(250), event -> {
            engine.run(1);
            gridBoard.render(engine.getBoard());
            if (onTickCallback != null) {
                onTickCallback.run();
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    /**
     * Resumes the existing simulation loop sequence if it has already been initialized.
     */
    public void continueLoop(){
        if(gameLoop!=null) startLoop();
    }

    /**
     * Halts the active JavaFX timeline animation loop, pausing the simulation progression.
     */
    public void stopLoop(){
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * Sets a notification callback to execute immediately after each simulation step completes.
     * @param callback the task listener to run on every tick
     */
    public void setOnTickCallback(Runnable callback) {
        onTickCallback = callback;
    }
    /**
     * Gets the internal simulation engine controller state instance.
     * @return the active SimulationEngine object
     */
    public SimulationEngine getEngine() {
        return engine;
    }
}