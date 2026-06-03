package Simulation.Controller;

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

        // 1. Gather user input from the console
        System.out.print("Podaj szerokość planszy: ");
        int width = readIntInput();

        System.out.print("Podaj wyskokość planszy: ");
        int height = readIntInput();

        System.out.print("Podaj liczbe robotnic: ");
        int workers = readIntInput();

        System.out.print("Podaj liczbe kwiatow: ");
        int flowers = readIntInput();

        this.engine = new SimulationEngine(width, height, workers, flowers);

        runConsoleMenu();
    }

    private void runConsoleMenu() {
        boolean running = true;
        System.out.println("\nUstawienia zapisane - wybierz tryb: [I] = pojedynczy krok, [X] = 10 kroków, [w] = wyjdź");

        while (running) {
            System.out.print("\nCommand: ");
            String command = scanner.next().toLowerCase();

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