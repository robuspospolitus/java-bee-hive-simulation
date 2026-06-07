module Bee.Simulation.main {
    requires javafx.controls;
    //requires javafx.fxml;
    requires java.desktop; // Needed if you are still using java.awt.Point

    // Crucial: You must allow JavaFX to access your packages
    exports org.example;      // Replace with your actual root package
    exports Simulation.View;  // Allows JavaFX to see and render your GUI components

    // If you plan to use FXML files down the line:

   opens Simulation.View to javafx.graphics;
}