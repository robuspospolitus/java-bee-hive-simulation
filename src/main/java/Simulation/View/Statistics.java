package Simulation.View;

import Simulation.Model.SimulationEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Statistics {

    private final int initialForagers;
    private final int initialStorers;
    private final double flowerChance;
    private final int finalTick;
    private final int finalHoney;
    private final int finalPollen;
    private final int finalForagers;
    private final int finalStorers;

    public Statistics(int initialStorers, int initialForagers, double flowerChance, SimulationEngine engine) {
        this.initialStorers = initialStorers;
        this.initialForagers = initialForagers;
        this.flowerChance = flowerChance;

        if (engine != null) {
            this.finalTick = engine.steps();
            this.finalHoney = engine.getHive() != null ? engine.getHive().getHoneyAmount() : 0;
            this.finalPollen = engine.getHive() != null ? engine.getHive().getPollenAmount() : 0;
            this.finalForagers = engine.getForagerCount();
            this.finalStorers = engine.getStorerCount();
        } else {
            this.finalTick = 0;
            this.finalHoney = 0;
            this.finalPollen = 0;
            this.finalForagers = 0;
            this.finalStorers = 0;
        }
    }

    public void saveToCsv(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            writer.println("Parametr,Wartosc");
            writer.println("Initial Foragers," + initialForagers);
            writer.println("Initial Storers," + initialStorers);
            writer.println("Flower Chance," + flowerChance);
            writer.println("Final Tick," + finalTick);
            writer.println("Final Honey," + finalHoney);
            writer.println("Final Pollen," + finalPollen);
            writer.println("Final Foragers," + finalForagers);
            writer.println("Final Storers," + finalStorers);
            System.out.println("Statystyki zostaly pomyślnie zapisane do pliku: " + filePath);
        } catch (IOException e) {
            System.err.println("Blad podczas zapisu statystyk do pliku CSV: " + e.getMessage());
        }
    }
}