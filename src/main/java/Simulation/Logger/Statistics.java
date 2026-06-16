package Simulation.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Simulation.Model.*;

public class Statistics {
    private final int initialForagers;
    private final int initialStorers;
    private final double flowerChance;
    private final List<Integer> ticks = new ArrayList<>();
    private final List<Integer> honeys = new ArrayList<>();
    private final List<Integer> pollens = new ArrayList<>();
    private final List<Integer> foragersList = new ArrayList<>();
    private final List<Integer> storersList = new ArrayList<>();

    public Statistics(int initialStorers, int initialForagers, double flowerChance) {
        this.initialStorers = initialStorers;
        this.initialForagers = initialForagers;
        this.flowerChance = flowerChance;
    }

    public void recordSnapshot(SimulationEngine engine) {
        if (engine != null) {
            ticks.add(engine.getCurrentTick());
            honeys.add(engine.getHive() != null ? engine.getHive().getHoneyAmount() : 0);
            pollens.add(engine.getHive() != null ? engine.getHive().getPollenAmount() : 0);
            foragersList.add(engine.getForagerCount());
            storersList.add(engine.getStorerCount());
        }
    }

    public void saveToCsv(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            print(writer, "Parameter", ticks);
            print(writer, "Initial Foragers", Collections.nCopies(ticks.size(), initialForagers));
            print(writer, "Initial Storers", Collections.nCopies(ticks.size(), initialStorers));
            print(writer, "Flower Chance", Collections.nCopies(ticks.size(), flowerChance));
            print(writer, "Honey", honeys);
            print(writer, "Pollen", pollens);
            print(writer, "Foragers", foragersList);
            print(writer, "Storers", storersList);
            Logger.log("Statistics saved successfully to: " + filePath);
        } catch (IOException e) {
            Logger.log("Error occurred while saving statistics: " + e.getMessage());
        }
    }
    private void print(PrintWriter writer, String text, List<? extends Number> list) {
        writer.print(text);
        for (Number value : list) writer.print(";" + value);
        writer.println();
    }
}