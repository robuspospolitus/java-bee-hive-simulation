package Simulation.Controller;

import Simulation.Model.SimulationEngine;
import Simulation.View.GridBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SimulationController {
    private SimulationEngine engine;
    private final GridBoard gridBoard;
    private Timeline gameLoop;
    private Runnable onTickCallback;

    public SimulationController(GridBoard gridBoard) {
        this.gridBoard = gridBoard;
    }

    public void start(int storers, int foragers, double flowerChance) {
        this.engine = new SimulationEngine(storers, foragers, flowerChance);
        startLoop();
    }

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

    public void continueLoop(){
        if(gameLoop!=null) startLoop();
    }

    public void stopLoop(){
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void setOnTickCallback(Runnable callback) {
        onTickCallback = callback;
    }
    public SimulationEngine getEngine() {
        return engine;
    }
}