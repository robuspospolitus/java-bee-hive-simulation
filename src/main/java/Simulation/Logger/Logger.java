package Simulation.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static String FILENAME;

    public static String createNewLogFile() {
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

        return FILENAME;
    }

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
