package Simulation.Model;
import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Forager;
import Simulation.Model.Agents.Storer;
import Simulation.Model.BoardCells.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private Board board;
    private List<Bee> agents;
    private int currentTick;
    private boolean isRunning;
    private int numWorkers;


    public SimulationEngine(int numWorkers, double flowerChance) {
        Cell.setFlowerChance(flowerChance);
        this.board = new Board(32, 16);
        this.agents = new ArrayList<>();
        this.currentTick = 0;
        this.numWorkers = numWorkers;
        initializeSimulation();
    }


    void initializeSimulation() {

        // 3. Spawn initial Foragers INSIDE the logical hive coordinates
        for (int i = 0; i < numWorkers; i++) {
            int startX = 15;
            int startY = 1;
            int ID= Bee.getTotalNum();
            Forager forager = new Forager(ID,10, startX, startY);
            addAgent(forager);
        }
        System.out.println("Simulation initialized");
    }

    public int steps() {
        if (!isRunning) return currentTick;
        List<Bee> currentAgents = new ArrayList<>(this.agents);

        for (Bee bee : currentAgents) {
            bee.move(this.board); //ruch, spadek energii i starzenie sie

            // Gathering pollen
            if (bee instanceof Forager) {
                Forager forager = (Forager) bee;
                Point pos = forager.getMovementContext().getPosition();
                Cell currentCell = board.getCell(pos.x, pos.y);

                if (currentCell != null && currentCell.hasFlower()) {
                    int collectedPollen = currentCell.takePollen(4);
                    if (collectedPollen > 0) {
                        forager.collectPollen(collectedPollen);
                    }
                }
            }
            if (bee.isDead()) {
                removeAgent(bee);
            }
        }

        board.regenerateEnvironment();

        this.currentTick++;
        System.out.println("Steps ran");
        return currentTick;
    }

    public void run(int totalSteps) {
        this.isRunning = true;
        System.out.println("Starting simulation execution for " + totalSteps + " steps.");

        for (int i = 0; i < totalSteps; i++) {
            steps();
        }
        this.isRunning = false;
        System.out.println("Simulation run completed.");
        System.out.println(totalSteps + " steps ran");

    }

    void addAgent(Bee bee) {
        this.agents.add(bee);

        // 1. Get the starting coordinates of the bee
        // Assuming your Forager/Bee class has a way to get its position:
        Point startPos = bee.getMovementContext().getPosition();

        // 2. Tell the board to place the bee. Old position is null!
        this.board.moveAgent(bee, null, startPos);

        // (Optional: Assuming your Bee has a getID() method)
        System.out.println("Dodano agenta o ID: " + bee.getID());
    }

    void removeAgent(Bee bee) {
        Point deadPos = bee.getBeePosition(); // gdzie pszcola umarla

        // 2. Jeśli pozycja istnieje, mówimy planszy, żeby ją zresetowała
        if (deadPos != null) {
            this.board.moveAgent((Bee) null, deadPos, (Point) null);
        }

        // 3. Usuwamy pszczołę z listy żywych agentów
        this.agents.remove(bee);
        System.out.println("Usunięto agenta o ID: ");
    }

    public Board getBoard (){return board;}

}

