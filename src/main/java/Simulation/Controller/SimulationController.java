package Simulation.Controller;

import Simulation.Model.SimulationEngine;
import java.util.Scanner;

public class SimulationController {
    private SimulationEngine engine;
    private final Scanner scanner;

    public SimulationController() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== BEE COLONY SIMULATION SETUP ===");

        // 1. Gather user input from the console
        System.out.print("Enter map width: ");
        int width = readIntInput();

        System.out.print("Enter map height: ");
        int height = readIntInput();

        System.out.print("Enter number of worker bees: ");
        int workers = readIntInput();

        System.out.print("Enter number of flowers: ");
        int flowers = readIntInput();

        // 2. Initialize the Engine
        this.engine = new SimulationEngine(width, height, workers, flowers);

        // 3. Start the console execution loop
        runConsoleMenu();
    }

    private void runConsoleMenu() {
        boolean running = true;
        System.out.println("\nSimulation ready! Controls: [s] = single step, [r] = run 10 steps, [q] = quit");

        while (running) {
            System.out.print("\nCommand: ");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "s":
                    int currentTick = engine.steps();
                    System.out.println("--- Tick " + currentTick + " ---");
                    // Optional: call a method to print the text map here
                    break;
                case "r":
                    System.out.println("Running 10 steps automatically...");
                    engine.run(10);
                    break;
                case "q":
                    running = false;
                    System.out.println("Exiting simulation.");
                    break;
                default:
                    System.out.println("Unknown command. Use 's', 'r', or 'q'.");
            }
        }
        scanner.close();
    }

    private int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number! Try again.");
            scanner.next(); // clear junk input
        }
        return scanner.nextInt();
    }
}