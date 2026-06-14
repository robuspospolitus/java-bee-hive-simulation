/*(package Simulation.Controller;
import Simulation.Model.SimulationEngine;
import java.util.Scanner;

public class SimulationController {
    private SimulationEngine engine;
    private final Scanner scanner;

    public SimulationController() {
        this.scanner = new Scanner(System.in);
        start();
    }

    public void start() {
        System.out.println("=== Ustawienia symulacji ===");

        System.out.print("Podaj liczbe robotnic: ");
        int workers = readIntInput();

        System.out.print("Podaj liczbe kwiatow: ");
        int flowers = readIntInput();

        this.engine = new SimulationEngine(workers, flowers);

        runConsoleMenu();
    }

    private void runConsoleMenu() {
        boolean running = true;
        System.out.println("\nUstawienia zapisane - wybierz tryb: [I] = pojedynczy krok, [X] = 10 kroków, [w] = wyjdź");

        while (running) {
            System.out.print("\nCommand: ");
            String command = scanner.next().toUpperCase().trim();

            switch (command) {
                case "I":
                    int currentTick = engine.steps();
                    System.out.println("--- Tick " + currentTick + " ---");
                    break;
                case "X":
                    System.out.println("Running 10 steps automatically...");
                    engine.run(10);
                    break;
                case "w":
                    running = false;
                    System.out.println("Exiting simulation.");
                    break;
                default:
                    System.out.println("Nieznana komenda (spróbuj: [I] = pojedynczy krok, [X] = 10 kroków, [w] = wyjdź)");
            }
        }
        scanner.close();
    }

    private int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number! Try again.");
            scanner.next();
        }
        return scanner.nextInt();
    }



}
*/

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

    // The View passes the drawing canvas to the controller so it can tell it when to redraw
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

            // Phase 1: Update the math
            engine.run(1); // Running 5 steps per frame (speeds up the simulation!)

            // Phase 2: Draw the new math to the screen
            gridBoard.render(engine.getBoard());

            // Phase 3: THE FIX - Tell the UI to update the numbers!
            if (onTickCallback != null) {
                onTickCallback.run();
            }
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    public void continueLoop(){
        if(gameLoop!=null)
        startLoop(); // ?
    }

    public void stopLoop(){
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void setOnTickCallback(Runnable callback) {
        this.onTickCallback = callback;
    }

    // 2. Add this getter so MainWindow can read the live stats
    public SimulationEngine getEngine() {
        return this.engine;
    }
}