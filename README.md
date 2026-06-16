# Multi-Agent Bee Simulation

[![pl](https://img.shields.io/badge/lang-pl-red.svg)](README.pl.md)

A simulation based on multi-agent architecture, modeling the behavior of a bee colony in a hive and its interactions with the external environment. The project is implemented in Java and demonstrates autonomous decision-making mechanisms of individual units in a distributed system.

## System Features

* **Queen:** Manages the hive structure, monitors resource status, controls the population, and coordinates the overall division of roles.
* **Foragers:** Responsible for map exploration, locating pollen sources, efficient resource harvesting, and return navigation.
* **Storers:** Receive harvested pollen from foragers at the hive entrance, transport it to storage chambers, and manage inventory.
* **Energy Management:** Each agent has a limited energy resource that is consumed during actions (flight, work). Dropping to zero energy requires returning to the hive for regeneration.
* **Autonomous Navigation:** Implementation of movement and pathfinding algorithms to optimize routes between the hive and flower fields.

## Project Structure

The project is implemented with a clean separation of concerns based on the MVC (Model-View-Controller) architectural pattern and the Java Platform Module System (JPMS). The directory structure in the main package is as follows:

* `src/main/java/Simulation/` - main logical module of the application
    * `Controller/` - intermediary layer responsible for controlling and managing the simulation flow (the `SimulationController` class).
    * `Logger/` - components responsible for logging system events (the `Logger` class) and generating statistics (the `Statistics` class).
    * `Model/` - logical core of the application, containing the world definition and behavioral rules
        * `Agents/` - implementation of the autonomous logic of individual bee types.
        * `BoardCells/` - structures representing individual cells and tiles on the board.
        * `MovementStrategies/` - algorithms and strategies for agent movement in space.
        * Base environment classes: `Board`, `Hive`, and simulation helper classes.
    * `View/` - presentation layer responsible for the graphical user interface (the `MainWindow` and `GridBoard` classes).
* `src/main/resources/` - resources directory, containing an `Images` subfolder with graphics and a `styles.css` file with styles used by the graphical user interface.

## System Requirements

* Java Development Kit (JDK) 17 or higher
* IntelliJ IDEA (recommended) or any other IDE supporting Java
* Gradle (optional, the project includes the Gradle Wrapper)

## Quick Start

1. **Cloning the Repository**

   For cloning the repository, enter the specific commands in your terminal to clone the repository to your device and navigate into the project directory:
    ```bash
   git clone https://github.com/robuspospolitus/java-bee-hive-simulation.git
   cd java-bee-hive-simulation
   ```
2. **Project compiling**

   For compiling the project, the application utilizes the Gradle Wrapper mechanism, so you do not need a local installation of Gradle on your machine to build it.
    ```bash
    ./gradlew build
    ```

3. **Running the application**

   For running the simulation, start the application using the Gradle application plugin executed in your terminal.
    ```bash
    ./gradlew run
    ```
## Sample Run

Upon a successful launch, the console will display environment initialization logs and a real-time dynamic log of actions performed by individual agents.
```plaintext
 A new log file has been created: simulation_2026-06-16_15-18-50.log
Simulation initialized
Starting simulation execution for 1 steps.
Storer 0 moves around the hive
Storer 0 random move performed, current position [10, 1]
Forager 1 is empty, full and goes through the exit to the meadow. Energy: 100.0
Forager 1 hates portals
Forager 2 heads towards the hive exit. Energy: 100.0
Forager 2 is flying to its destination. Left: 0.0
Forager 3 heads towards the hive exit. Energy: 100.0
Forager 3 is flying to its destination. Left: 1.0
The eggs were laid by the queen on 4, 7
Steps ran
Simulation run completed.
1 steps ran
```

In addition to the console output, a log file will be created in the project directory, including timestamps for each event.
```plaintext
[15:18:50] Simulation initialized
[15:18:50] Starting simulation execution for 1 steps.
[15:18:50] Storer 0 moves around the hive
[15:18:50] Storer 0 random move performed, current position [10, 1]
[15:18:50] Forager 1 is empty, full and goes through the exit to the meadow. Energy: 100.0
[15:18:50] Forager 1 hates portals
[15:18:50] Forager 2 heads towards the hive exit. Energy: 100.0
[15:18:50] Forager 2 is flying to its destination. Left: 0.0
[15:18:50] Forager 3 heads towards the hive exit. Energy: 100.0
[15:18:50] Forager 3 is flying to its destination. Left: 1.0
[15:18:50] The eggs were laid by the queen on 4, 7
[15:18:50] Steps ran
[15:18:50] Simulation run completed.
[15:18:50] 1 steps ran
```

Every 10 simulation ticks, new columns of current statistics are appended to the simulation statistics CSV file.

## Simulation Configuration

Apart from the actual variables available directly in the application's user GUI, additional parameters such as bee lifespan and energy consumption can be configured in the file located at `src/main/java/Simulation/Model/SimulationConfig.java`.