package Simulation.Model;
import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Forager;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private Board board;
    private List<Bee> agents;
    private int currentTick;
    private boolean isRunning;
    private int numWorkers;
    private int numFlowers;


    public SimulationEngine (int width, int height, int numWorkers, int numFlowers){
    this.board = new Board(width, height);
        this.agents = new ArrayList<>();
        this.currentTick = 0;
        this.numWorkers = numWorkers;
        this.numFlowers = numFlowers;
        initializeSimulation();
    }

   // SimulationEngine sim = new SimulationEngine(int width, int height, int numWorkers,int numFlowers);

void initializeSimulation(){
    for (int i =0; i < numWorkers ; i++){

        Bee worker = new Forager(i,10);
        addAgent(worker);

    }
        System.out.println("Simulation initialized");
}

int steps(){
    if (!isRunning) return currentTick;

    // 1. Create a copy of the list for safe loop execution
    List<Bee> currentAgents = new ArrayList<>(this.agents);

    // 2. Let every bee perceive, decide, and move
    for (Bee bee : currentAgents) {
        bee.step(this.board); // Pass the board so they can check surrounding cells

        // Example logic: Check if the bee ran out of energy during its move
        if (bee.getEnergy() <= 0) {
            removeAgent(bee);
        }
    }

    //UPDATE ENVIRONMENT
    // board.regeneratePollen();

    this.currentTick++;
    return currentTick;
    System.out.println("Steps ran");
    return 0;
}

void run(int totalSteps){
    this.isRunning = true;
    System.out.println("Starting simulation execution for " + totalSteps + " steps.");

    for (int i = 0; i < totalSteps; i++) {
        steps();
    this.isRunning = false;
    System.out.println("Simulation run completed.");
}
        System.out.println("Steps ran");
}

void addAgent(Bee bee){

    void addAgent(Bee bee) {
        this.agents.add(bee);
      //  this.board.moveAgent(bee, null, bee.getCoordinates());
        System.out.println("Dodano agenta o ID: " + bee.getId());
  }

void removeAgent (Bee bee){
    this.agents.remove(bee);
   // this.board.moveAgent(null, bee.getCoordinates(), null);
    System.out.println("Usunięto agenta o ID: " + bee.getId());
}

}
