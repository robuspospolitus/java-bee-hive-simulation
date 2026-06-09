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

    // The View passes the drawing canvas to the controller so it can tell it when to redraw
    public SimulationController(GridBoard gridBoard) {
        this.gridBoard = gridBoard;
    }

    public void start(int storers, int foragers, double flowerChance) {

        this.engine = new SimulationEngine(storers, foragers, flowerChance);

        startLoop();
    }

    private void startLoop() {
        // Safety check: if they click "Start" twice, stop the old loop first
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Create a timer that ticks every 250 milliseconds (4 frames per second)
        gameLoop = new Timeline(new KeyFrame(Duration.millis(250), event -> {

            // Phase 1: Update the math (Model)
            engine.run(5);

            // Phase 2: Draw the new math to the screen (View)
            gridBoard.render(engine.getBoard());

        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE); // Tell it to loop forever
        gameLoop.play();

    }

    public void stopLoop(){
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}