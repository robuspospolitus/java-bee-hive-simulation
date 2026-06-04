package Simulation.View;

import Simulation.Controller.SimulationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        // 3. Create a simple button to launch it
        Button startBtn = new Button("Start Simulation");
        startBtn.setOnAction(event -> controller.start());

        // 4. Stack them in a simple layout (Button on top, Map on bottom)
        VBox layout = new VBox(10, startBtn, gridBoard);

        Scene scene = new Scene(layout, 720, 512);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test Run");
        primaryStage.show();
    }
   /* @Override
    public void start(Stage primaryStage) {
        // 1. Create your UI components
        Button startButton = new Button("Start Simulation");
        Button stepButton = new Button("Next Step");

        // Let's assume you created a GridBoard class that extends javafx.scene.canvas.Canvas
        GridBoard gridBoard = new GridBoard(800, 600);

        // 2. Initialize your existing Controller!
        // Notice how we pass the UI elements to the controller so it can manage them
        SimulationController controller = new SimulationController();

        // 3. Hook up the buttons to the controller's methods
        //startButton.setOnAction(event -> controller.start());
        //stepButton.setOnAction(event -> controller.executeSingleStep());

        // 4. Layout the window (Buttons on left, Grid on right)
        VBox menu = new VBox(10, startButton, stepButton);
        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(gridBoard);

        // 5. Show the window
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Bee Colony Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void launchGUI(String[] args) {
        launch(args); // This is a method inherited from javafx.application.Application
    }*/
}