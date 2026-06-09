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

        Spinner<Integer> workerSpinner = new Spinner<>(1, 20, 10);
        Spinner<Double> flowerSpinner = new Spinner<>(0.01, 0.9, 0.1, 0.02);

        // 3. Create a simple button to launch it
        Button startBtn = new Button("Start Simulation");
        startBtn.setOnAction(event -> {
            // Just grab the numbers directly from the boxes when they click start!
            int workers = workerSpinner.getValue();
            Double flowers = flowerSpinner.getValue();

            controller.start(workers, flowers);
        });
        Button stopBtn = new Button("Stop Simulation");
        stopBtn.setOnAction(event -> controller.stopLoop());
        Button continueBtn = new Button("Continue Old Simulation");
        continueBtn.setOnAction(event -> controller.continueLoop());

        // 4. Stack them in a simple layout (Button on top, Map on bottom)

        VBox controls = new VBox(5, startBtn, stopBtn, continueBtn,
                new Label("Number of Workers:"), workerSpinner,
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