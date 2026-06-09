package Simulation.Model;
import Simulation.Model.Agents.*;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private Board board;
    private List<Bee> agents;
    private int currentTick;
    private boolean isRunning;
    private int numStorers;
    private int numForagers;


    public SimulationEngine(int numStorers, int numForagers, double flowerChance) {
        Cell.setFlowerChance(flowerChance);
        this.board = new Board(32, 16);
        this.agents = new ArrayList<>();
        this.currentTick = 0;
        this.numStorers = numStorers;
        this.numForagers = numForagers;
        initializeSimulation();
    }


    void initializeSimulation() {
        // Spawn initial Foragers INSIDE the logical hive coordinates
        int defaultSpawnX = 16;
        int defaultSpawnY = 1;

        // Storers spawn
        for (int i = 0; i < numStorers; i++) {
            Point safePos = findEmptySpawnPosition(this.board, defaultSpawnX, defaultSpawnY);
            Storer newBee = new Storer(i, 0, safePos.x, safePos.y);

            this.agents.add(newBee);
            this.board.getCell(safePos.x, safePos.y).setAgent(newBee);
        }
        //Foragers spawn
        for (int i = 0; i < numForagers; i++) {
            Point safePos = findEmptySpawnPosition(this.board, defaultSpawnX, defaultSpawnY);
            Forager newBee = new Forager(i, 10, safePos.x, safePos.y);
            this.agents.add(newBee);
            this.board.getCell(safePos.x, safePos.y).setAgent(newBee);
        }
        // Queen spawn
        Queen queen = new Queen(Bee.getTotalNum(), 1, 5, 8);
        this.agents.add(queen);
        this.board.setQueen(queen);
        this.board.getCell(5, 8).setAgent(queen);
        System.out.println("Simulation initialized");
    }

    public int steps() {
        if (!isRunning) return currentTick;
        for (int i = this.agents.size() - 1; i >= 0; i--) { //lecimy od tylu by wyrzucanie agentow nie psulo dzialania fora
            Bee bee = this.agents.get(i);
            // TRANSFORM LARVAE INTO STORERS (feeding is automatic each tick)
            if (bee instanceof Larva larva) {
                if (larva.isReadyToTransform()) {
                    Point larvaPos = larva.getBeePosition();
                    int larvaId = larva.getID();
                    removeAgent(larva);
                    Storer newStorer = new Storer(larvaId, 0, larvaPos.x, larvaPos.y);
                    addAgent(newStorer);
                    continue;
                }
                else larva.beFed();
            }
            // TRANSFORM STORERS INTO FORAGERS IF OLD ENOUGH
            if (bee instanceof Storer storer) {
                if (storer.isReadyToEvolve()) {
                    Point storerPos = storer.getMovementContext().getPosition();
                    int storerId = storer.getID();
                    removeAgent(storer);
                    Forager newForager = new Forager(storerId, 0, storerPos.x, storerPos.y);
                    addAgent(newForager);
                    System.out.println("Magazynierka " + storerId + " stala sie Zbieraczka!");
                    continue;
                }
            }

            bee.move(this.board); // ruch, spadek energii i starzenie sie

            // Gathering pollen
            Point pos = bee.getBeePosition();
            Cell currentCell = board.getCell(pos.x, pos.y);
            bee.interact(currentCell);

            if (bee.isDead()) {
                removeAgent(bee);
            }
        }


        Queen queen = board.getQueen();
        if (queen != null && queen.canLayEgg()) {
            int newId = this.agents.size();

            // 1. Get the Queen's current location
            Point queenPos = queen.getBeePosition();

            // 2. Find an empty spot nearby for the egg!
            Point nurseryPos = findEmptySpawnPosition(this.board, queenPos.x, queenPos.y);

            // 3. Create the Larva
            Larva newLarva = queen.layEggs(newId, nurseryPos.x, nurseryPos.y);

            // 4. IMPORTANT: Update the new Larva's position to the empty spot!
            // (Assuming your Larva/Bee class has a way to set its position, like this:)
            newLarva.getMovementContext().setPosition(nurseryPos);

            // 5. Now add it to the board at the SAFE position
            this.agents.add(newLarva);
            this.board.moveAgent(newLarva, null, nurseryPos);

            queen.resetEggCooldown();
        }

        if (this.currentTick % SimulationConfig.POLLEN_REGENERATION_TIME == 0) {
            board.regenerateEnvironment();
        }

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
        System.out.println("Usunięto agenta o ID: "+ bee.getID());
    }

    public Board getBoard (){return board;}

    private Point findEmptySpawnPosition(Board board, int startX, int startY) {
        int maxRadius = Math.max(board.getWidth(), board.getHeight());

        for (int radius = 0; radius < maxRadius; radius++) {
            for (int x = startX - radius; x <= startX + radius; x++) {
                for (int y = startY - radius; y <= startY + radius; y++) {
                    boolean isInsideHive = x >= 0 && x < 12;
                    boolean isWithinHeight = y >= 0 && y < board.getHeight();

                    if (isInsideHive && isWithinHeight) {
                        Cell cell = board.getCell(x, y);
                        if (cell != null && cell.getType() != CellType.OBSTACLE && cell.getAgent() == null) {
                            return new Point(x, y);
                        }
                    }
                }
            }
        }
        return new Point(startX, startY);
    }
}

