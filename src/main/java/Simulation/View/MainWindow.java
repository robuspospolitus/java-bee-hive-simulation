package Simulation.View;

import Simulation.Controller.SimulationController;
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

        // 1. Create the drawing canvas (e.g., 40 tiles * 20px = 800px width)
        GridBoard gridBoard = new GridBoard(32, 16);

        // 2. Create the Controller and give it the canvas
        SimulationController controller = new SimulationController(gridBoard);

        Spinner<Integer> storerSpinner = new Spinner<>(1, 20, 1);
        Spinner<Integer> foragerSpinner = new Spinner<>(1, 20, 3);
        Spinner<Double> flowerSpinner = new Spinner<>(0.01, 0.9, 0.01, 0.02);


        this.controller = controller;

        //update collected data
        this.controller.setOnTickCallback(() -> {
            updateDashboard();
        });

        // 3. Create a simple button to launch it
        Button startBtn = new Button("Start New Simulation");
        startBtn.setOnAction(event -> {
            // Just grab the numbers directly from the boxes when they click start!
            int foragers = foragerSpinner.getValue();
            int storers = storerSpinner.getValue();
            Double flowers = flowerSpinner.getValue();

            controller.start(storers, foragers, flowers);
        });

        Button stopBtn = new Button("Stop Simulation");
        stopBtn.setOnAction(event -> controller.stopLoop());

        Button continueBtn = new Button("Continue Old Simulation");
        continueBtn.setOnAction(event -> controller.continueLoop());



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


    private void updateDashboard() {
        // Fetch the active engine from the controller
        SimulationEngine engine = controller.getEngine();

        // Safety check to make sure the simulation actually started
        if (engine == null) return;

        Hive hive = engine.getHive();
        if (hive != null) {
            honeyLabel.setText("Honey: " + hive.getHoneyAmount());
            pollenLabel.setText("Pollen: " + hive.getPollenAmount());
        }

        foragerLabel.setText("Foragers: " + engine.getForagerCount());
        storerLabel.setText("Storers: " + engine.getStorerCount());

        // Because you run 5 steps per frame, this will jump by 5s!
        tickLabel.setText("Tick: " + engine.steps());
    }
}