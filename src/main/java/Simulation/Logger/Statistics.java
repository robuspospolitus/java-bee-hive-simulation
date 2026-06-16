package Simulation.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Simulation.Model.*;

/**
 * Manages historical performance metrics and environmental states throughout the simulation lifecycle, providing functionality to export data snapshots into CSV format.
 */
public class Statistics {
    private final int initialForagers;
    private final int initialStorers;
    private final double flowerChance;
    private final List<Integer> ticks = new ArrayList<>();
    private final List<Integer> honeys = new ArrayList<>();
    private final List<Integer> pollens = new ArrayList<>();
    private final List<Integer> foragersList = new ArrayList<>();
    private final List<Integer> storersList = new ArrayList<>();

    /**
     * Initializes a new Statistics instance with the starting parameters of the simulation.
     * @param initialStorers the starting number of storer bees in the simulation
     * @param initialForagers the starting number of forager bees in the simulation
     * @param flowerChance the probability factor configured for flower generation
     */
    public Statistics(int initialStorers, int initialForagers, double flowerChance) {
        this.initialStorers = initialStorers;
        this.initialForagers = initialForagers;
        this.flowerChance = flowerChance;
    }

    /**
     * Captures and appends a single metrics snapshot from the current state of the simulation engine
     * @param engine the active simulation engine instance providing the data to record
     */
    public void recordSnapshot(SimulationEngine engine) {
        if (engine != null) {
            ticks.add(engine.getCurrentTick());
            honeys.add(engine.getHive() != null ? engine.getHive().getHoneyAmount() : 0);
            pollens.add(engine.getHive() != null ? engine.getHive().getPollenAmount() : 0);
            foragersList.add(engine.getForagerCount());
            storersList.add(engine.getStorerCount());
        }
    }

    /**
     * Exports all collected simulation statistics into a CSV file at the specified file location.
     * @param filePath the destination path where the CSV data file will be written
     */
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

    /**
     * Helper method that formats and writes a single row of numeric data into the CSV file.
     * @param writer the PrintWriter instance handling the file output stream
     * @param text the label representing the data parameter in the current row
     * @param list the collection of numerical records to be appended to the row
     */
    private void print(PrintWriter writer, String text, List<? extends Number> list) {
        writer.print(text);
        for (Number value : list) writer.print(";" + value);
        writer.println();
    }
}