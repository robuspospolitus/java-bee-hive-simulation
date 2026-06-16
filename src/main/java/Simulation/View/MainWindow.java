package Simulation.View;

import Simulation.Controller.SimulationController;
import Simulation.Logger.Statistics;
import Simulation.Model.Hive;
import Simulation.Model.SimulationEngine;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Main application window for the bee simulation, responsible for initializing the JavaFX
 * user interface components, external CSS stylesheets, control panels, and real-time dashboard layouts.
 */
public class MainWindow extends Application {
    private SimulationController controller;
    private Statistics currentStats;
    private Label honeyLabel, pollenLabel, foragerLabel, storerLabel, tickLabel;
    private Spinner<Integer> storerSpinner, foragerSpinner;
    private Spinner<Double> flowerSpinner;

    /**
     * Initializes the primary stage layout, setting up controls, registration of callback handlers,and coordinates the positioning of dashboard layouts.
     * @param primaryStage the primary stage for this JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        GridBoard gridBoard = new GridBoard(32, 16);
        this.controller = new SimulationController(gridBoard);
        this.controller.setOnTickCallback(this::updateDashboard);

        VBox controlsPanel = createControlsPanel();
        VBox dashboardPanel = createDashboardPanel();

        BorderPane layout = new BorderPane();
        layout.setLeft(controlsPanel);
        layout.setCenter(gridBoard);
        layout.setRight(dashboardPanel);

        Scene scene = new Scene(layout, 1440, 640);

        URL styleUrl = getClass().getResource("/styles.css");
        if (styleUrl != null) {
            scene.getStylesheets().add(styleUrl.toExternalForm());
        } else {
            System.err.println("Did not find file style.css");
        }

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/Images/Bee.png"));
        primaryStage.setTitle("Bee Simulation");
        primaryStage.show();
    }

    /**
     * Generates the simulation control panel equipped with interactive buttons and parameter configuration spinners.
     * @return a configured VBox container handling input controls
     */
    private VBox createControlsPanel() {
        Label controlsTitle = new Label("Control Panel");
        controlsTitle.getStyleClass().add("title");
        controlsTitle.setMaxWidth(Double.MAX_VALUE);

        initSpinners();

        Button startBtn = new Button("Start New Simulation");
        startBtn.getStyleClass().add("primary-btn");
        startBtn.setOnAction(_ -> handleStartAction());

        Button stopBtn = new Button("Stop Simulation");
        stopBtn.getStyleClass().add("danger-btn");
        stopBtn.setOnAction(_ -> handleStopAction());

        Button continueBtn = new Button("Continue Old Simulation");
        continueBtn.getStyleClass().add("secondary-btn");
        continueBtn.setOnAction(_ -> controller.continueLoop());

        Label foragerTitle = new Label("Number of Foragers:");
        Label storerTitle = new Label("Number of Storers:");
        Label flowerTitle = new Label("Flower Spawn Chance:");

        foragerTitle.getStyleClass().add("text-label");
        storerTitle.getStyleClass().add("text-label");
        flowerTitle.getStyleClass().add("text-label");

        VBox controls = new VBox(10, controlsTitle, startBtn, stopBtn, continueBtn,
                foragerTitle, foragerSpinner,
                storerTitle, storerSpinner,
                flowerTitle, flowerSpinner);
        controls.getStyleClass().add("controls-panel");
        controls.setPrefWidth(220);
        controls.setAlignment(Pos.TOP_CENTER);
        return controls;
    }

    /**
     * Creates and styles the dashboard panel containing real-time simulation metric readouts.
     * @return a configured VBox layout wrapper containing statistical labels
     */
    private VBox createDashboardPanel() {
        Label statsTitle = new Label("Simulation Stats");
        statsTitle.getStyleClass().add("title");
        statsTitle.setMaxWidth(Double.MAX_VALUE);

        tickLabel = new Label("Tick: 0");
        honeyLabel = new Label("Honey: 0");
        pollenLabel = new Label("Pollen: 0");
        foragerLabel = new Label("Foragers: 0");
        storerLabel = new Label("Storers: 0");

        tickLabel.getStyleClass().add("tick-value");
        honeyLabel.getStyleClass().add("metric-value");
        pollenLabel.getStyleClass().add("metric-value");
        foragerLabel.getStyleClass().add("metric-value");
        storerLabel.getStyleClass().add("metric-value");

        VBox dashboard = new VBox(15);
        dashboard.getStyleClass().add("dashboard-panel");
        dashboard.setPrefWidth(220);
        dashboard.getChildren().addAll(statsTitle, tickLabel, honeyLabel, pollenLabel, foragerLabel, storerLabel);
        return dashboard;
    }

    /**
     * Initializes numeric range limitations and configures custom style classes for value input elements.
     */
    private void initSpinners() {
        storerSpinner = new Spinner<>(1, 20, 1);
        foragerSpinner = new Spinner<>(1, 20, 3);
        flowerSpinner = new Spinner<>(0.01, 0.9, 0.01, 0.02);

        storerSpinner.getStyleClass().add("spinner");
        foragerSpinner.getStyleClass().add("spinner");
        flowerSpinner.getStyleClass().add("spinner");
    }

    /**
     * Reads selected parameters from user spinners, creates statistics tracking, and signals the loop controller to execute the setup.
     */
    private void handleStartAction() {
        int foragers = foragerSpinner.getValue();
        int storers = storerSpinner.getValue();
        Double flowers = flowerSpinner.getValue();

        currentStats = new Statistics(storers, foragers, flowers);
        controller.start(storers, foragers, flowers);
    }

    /**
     * Instructs the loop engine to stop ongoing loop and saves statistics into the local tracking file.
     */
    private void handleStopAction() {
        controller.stopLoop();
        if (currentStats != null) {
            currentStats.saveToCsv("simulation_statistics.csv");
        }
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