package Simulation.View;

import Simulation.Controller.SimulationController;
import Simulation.Logger.Statistics;
import Simulation.Model.Hive;
import Simulation.Model.SimulationEngine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private SimulationController controller;
    private Label honeyLabel;
    private Label pollenLabel;
    private Label foragerLabel;
    private Label storerLabel;
    private Label tickLabel;
    private Statistics currentStats;

    /**
     * Initializes the primary stage layout, setting up controls, registration of callback handlers, and coordinates the positioning of dashboard layouts.
     * @param primaryStage the primary stage for this JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        tickLabel = new Label("Tick: 0");
        honeyLabel = new Label("Honey: 0");
        pollenLabel = new Label("Pollen: 0");
        foragerLabel = new Label("Foragers: 0");
        storerLabel = new Label("Storers: 0");

        String style = "-fx-font-size: 16px; -fx-font-weight: bold;";
        tickLabel.setStyle(style);
        honeyLabel.setStyle(style);
        pollenLabel.setStyle(style);
        foragerLabel.setStyle(style);
        storerLabel.setStyle(style);

        VBox dashboard = new VBox(10);
        dashboard.setPadding(new Insets(15));
        dashboard.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 2;");
        dashboard.getChildren().addAll(tickLabel, honeyLabel, pollenLabel, foragerLabel, storerLabel);

        // Drawing canvas (e.g., 40 tiles * 20px = 800px width)
        GridBoard gridBoard = new GridBoard(32, 16);

        // Controller with the canvas
        SimulationController controller = new SimulationController(gridBoard);

        Spinner<Integer> storerSpinner = new Spinner<>(1, 20, 1);
        Spinner<Integer> foragerSpinner = new Spinner<>(1, 20, 3);
        Spinner<Double> flowerSpinner = new Spinner<>(0.01, 0.9, 0.01, 0.02);

        this.controller = controller;

        // Update collected data
        this.controller.setOnTickCallback(() -> updateDashboard());

        // Launch button
        Button startBtn = new Button("Start New Simulation");
        startBtn.setOnAction(_ -> {
            // Get values from user inputs
            int foragers = foragerSpinner.getValue();
            int storers = storerSpinner.getValue();
            Double flowers = flowerSpinner.getValue();

            currentStats = new Statistics(storers, foragers, flowers);
            controller.start(storers, foragers, flowers);
        });

        // Stop button
        Button stopBtn = new Button("Stop Simulation");
        stopBtn.setOnAction(_ -> {
            controller.stopLoop();
            if (currentStats != null) {
                currentStats.saveToCsv("simulation_statistics.csv");
            }
        });

        // Continue button
        Button continueBtn = new Button("Continue Old Simulation");
        continueBtn.setOnAction(_ -> controller.continueLoop());

        // Inputs
        VBox controls = new VBox(5, startBtn, stopBtn, continueBtn,
                new Label("Number of Foragers:"), foragerSpinner,
                new Label("Number of Storers:"), storerSpinner,
                new Label("Chance for flower appearing:"), flowerSpinner);

        BorderPane layout = new BorderPane();
        layout.setLeft(controls);
        layout.setCenter(gridBoard);
        layout.setRight(dashboard);

        layout.setStyle("-fx-padding: 10px;");
        Scene scene = new Scene(layout, 1440, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bee Simulation");
        primaryStage.show();
    }

    /**
     * Retrieves the latest operational metrics from the simulation engine and synchronizes the text readouts displayed on the dashboard metrics label panel.
     */
    private void updateDashboard() {
        SimulationEngine engine = controller.getEngine();
        if (engine == null) return;

        Hive hive = engine.getHive();
        if (hive != null) {
            honeyLabel.setText("Honey: " + hive.getHoneyAmount());
            pollenLabel.setText("Pollen: " + hive.getPollenAmount());
        }
        foragerLabel.setText("Foragers: " + engine.getForagerCount());
        storerLabel.setText("Storers: " + engine.getStorerCount());

        int currentTick = engine.getCurrentTick();
        tickLabel.setText("Tick: " + engine.getCurrentTick());

        if (currentTick > 0 && currentTick % 10 == 0 && currentStats != null) {
            currentStats.recordSnapshot(engine);
            currentStats.saveToCsv("simulation_statistics.csv");
        }
    }
}