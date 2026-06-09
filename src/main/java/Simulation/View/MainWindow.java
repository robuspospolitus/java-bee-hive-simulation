package Simulation.View;

import Simulation.Controller.SimulationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Create the drawing canvas (e.g., 40 tiles * 20px = 800px width)
        GridBoard gridBoard = new GridBoard(32, 16);

        // 2. Create the Controller and give it the canvas
        SimulationController controller = new SimulationController(gridBoard);
        Spinner<Integer> storerSpinner = new Spinner<>(1, 20, 1);
        Spinner<Integer> foragerSpinner = new Spinner<>(1, 20, 3);
        Spinner<Double> flowerSpinner = new Spinner<>(0.01, 0.9, 0.01, 0.02);

        // 3. Create a simple button to launch it
        Button startBtn = new Button("Start Simulation");
        startBtn.setOnAction(event -> {
            // Just grab the numbers directly from the boxes when they click start!
            int foragers = foragerSpinner.getValue();
            int storers = storerSpinner.getValue();
            Double flowers = flowerSpinner.getValue();

            controller.start(storers, foragers, flowers);
        });
        Button stopBtn = new Button("Stop Simulation");
        stopBtn.setOnAction(event -> controller.stopLoop());

        VBox controls = new VBox(5, startBtn, stopBtn,
                new Label("Number of Foragers:"), foragerSpinner,
                new Label("Number of Storers:"), storerSpinner,
                new Label("Chance for flower appearing:"), flowerSpinner);

        BorderPane layout = new BorderPane();
        layout.setLeft(controls);
        layout.setCenter(gridBoard);

        layout.setStyle("-fx-padding: 10px;");
        //layout.getChildren().addAll(startBtn, stopBtn);
        Scene scene = new Scene(layout, 1280, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bee Simulation");
        primaryStage.show();
    }

}