package Simulation.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles writing simulation runtime events and milestones to a time-stamped external log file and synchronizing output to the standard terminal.
 */
public class Logger {
    private static String FILENAME;

    /**
     * Creates a new log file named by the date it was created on.
     */
    public static void createNewLogFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        FILENAME = "simulation_" + LocalDateTime.now().format(formatter) + ".log";

        try {
            File file = new File(FILENAME);
            if (file.createNewFile()) {
                System.out.println("A new log file has been created: " + FILENAME);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the log file: " + e.getMessage());
        }
    }

    /**
     * Creates a new entry in the currently used .log file (with a timestamp) and terminal (without timestamp)
     * @param message String to print out
     */
    public static void log(String message) {
        if (FILENAME == null || FILENAME.isEmpty()) {
            System.out.println(message);
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME, true))) {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            writer.println("[" + timeStamp + "] " + message);
            System.out.println(message);
        } catch (IOException e) {
            System.err.println("An error occurred while saving the log: " + e.getMessage());
        }
    }
}
