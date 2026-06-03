package Simulation.Model;
import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Forager;
import Simulation.Model.Agents.Storer;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private Board board;
    private List<Bee> agents;
    private int currentTick;
    private boolean isRunning;
    private int numWorkers;
    private int numFlowers;


    public SimulationEngine(int width, int height, int numWorkers, int numFlowers) {
        this.board = new Board();
        this.agents = new ArrayList<>();
        this.currentTick = 0;
        this.numWorkers = numWorkers;
        this.numFlowers = numFlowers;
        initializeSimulation();
    }


    void initializeSimulation() {

        for (int i = 0; i < numWorkers / 2; i++) {

            Bee forager = new Forager(10);
            Bee storer = new Storer(0);
            addAgent(forager);
            addAgent(storer);
        }
        System.out.println("Simulation initialized");
    }

    public int steps() {
        if (!isRunning) return currentTick;
            List<Bee> currentAgents = new ArrayList<>(this.agents);

            for (Bee bee : currentAgents) {
                bee.move(this.board);
                //if (bee.getEnergy() <= 0) {
                //    removeAgent(bee);
                //}
            }

            //UPDATE ENVIRO
            // board.regeneratePollen();

            this.currentTick++;
            System.out.println("Steps ran");
           return currentTick;
    }

    public void run(int totalSteps) {
        this.isRunning = true;
        System.out.println("Starting simulation execution for " + totalSteps + " steps.");

        for (int i = 0; i < totalSteps; i++) {
            steps();
            this.isRunning = false;
            System.out.println("Simulation run completed.");
        }
        System.out.println("Steps ran");
    }

    void addAgent(Bee bee) {
            this.agents.add(bee);
            //  this.board.moveAgent(bee, null, bee.getCoordinates());
            System.out.println("Dodano agenta o ID: ");
        }

        void removeAgent (Bee bee){
            //this.agents.remove(bee);
            //this.board.moveAgent(null, bee.getCoordinates(), null);
            System.out.println("Usunięto agenta o ID: ");
        }

    }

