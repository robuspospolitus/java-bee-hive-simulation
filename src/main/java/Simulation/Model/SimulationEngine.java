package Simulation.Model;
import Simulation.Model.Agents.*;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.BoardCells.CellType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationEngine {
    private Board board;
    private List<Bee> agents;
    private int currentTick;
    private boolean isRunning;
    private int numStorers;
    private int numForagers;

    public SimulationEngine(int numStorers, int numForagers, double flowerChance) {
        Cell.setFlowerChance(flowerChance);
        board = new Board(32, 16);
        agents = new ArrayList<>();
        currentTick = 0;
        this.numStorers = numStorers;
        this.numForagers = numForagers;
        initializeSimulation();
    }

    void initializeSimulation() {
        int defaultSpawnX = 16;
        int defaultSpawnY = 1;

        // Storers spawn
        spawnBees(numStorers, 0, defaultSpawnX, defaultSpawnY, Storer::new);
        // Foragers spawn
        spawnBees(numForagers, 10, defaultSpawnX, defaultSpawnY, Forager::new);
        // Queen spawn
        Queen queen = new Queen(Bee.getTotalNum(), 1, 5, 8);
        agents.add(queen);
        board.setQueen(queen);
        board.getCell(5, 8).setAgent(queen);
        System.out.println("Simulation initialized");
    }

    public int steps() {
        if (!isRunning) return currentTick;

        HashMap<Bee, DeathType> toRemove = new HashMap<>();
        Map<DeathType, String> deathReason = Map.of(
                DeathType.TRANSFORMATION, "has gone through tranformation",
                DeathType.OLD_AGE, "died of old age",
                DeathType.STARVATION, "died of starvation"
        );
        List<Bee> toAdd = new ArrayList<>();

        // Check state of all bees
        for (Bee bee : agents) {
            // TRANSFORM LARVAE INTO STORERS (feeding is automatic each tick)
            if (bee instanceof Larva larva) {
                if (larva.isReadyToTransform()) {
                    Point larvaPos = larva.getBeePosition();
                    toRemove.put(larva, DeathType.TRANSFORMATION);
                    toAdd.add(new Storer(larva.getID(), 0, larvaPos.x, larvaPos.y));
                    continue;
                }
            }
            // TRANSFORM STORERS INTO FORAGERS IF OLD ENOUGH
            if (bee instanceof Storer storer) {
                if (storer.isReadyToEvolve()) {
                    Point storerPos = storer.getMovementContext().getPosition();
                    toRemove.put(storer, DeathType.TRANSFORMATION);
                    toAdd.add(new Forager(storer.getID(), 0, storerPos.x, storerPos.y));
                    System.out.println("Magazynierka " + storer.getID() + " stala sie Zbieraczka!");
                    continue;
                }
            }

            bee.move(board); // ruch, spadek energii i starzenie sie

            if (bee.isDead()) {
                toRemove.put(bee, DeathType.STARVATION);
                continue;
            }
            if (bee.isTooOld()) {
                toRemove.put(bee, DeathType.OLD_AGE);
                continue;
            }

            // Gathering pollen
            Point pos = bee.getBeePosition();
            Cell currentCell = board.getCell(pos.x, pos.y);
            bee.interact(currentCell);
        }

        // removing and adding bees
        toRemove.forEach(((bee, deathType) -> {
            removeAgent(bee);
            System.out.println("Bee of ID "+ bee.getID() + " " + deathReason.get(deathType));
        }));
        for (Bee bee : toAdd) { addAgent(bee); }

        // laying eggs
        Queen queen = board.getQueen();
        if (queen != null && queen.canLayEgg()) {
            int newId = Bee.getTotalNum();

            Point queenPos = queen.getBeePosition();
            Point nurseryPos = findEmptySpawnPosition(board, queenPos.x, queenPos.y);
            Larva newLarva = queen.layEggs(newId, nurseryPos.x, nurseryPos.y);
            newLarva.getMovementContext().setPosition(nurseryPos);
            agents.add(newLarva);
            board.moveAgent(newLarva, null, nurseryPos);

            queen.resetEggCooldown();
        }

        if (currentTick % SimulationConfig.POLLEN_REGENERATION_TIME == 0) {
            board.regenerateEnvironment();
        }

        currentTick++;
        System.out.println("Steps ran\n");
        return currentTick;
    }

    public void run(int totalSteps) {
        isRunning = true;
        System.out.println("Starting simulation execution for " + totalSteps + " steps.");

        for (int i = 0; i < totalSteps; i++) {
            steps();
        }
        isRunning = false;
        System.out.println("Simulation run completed.");
        System.out.println(totalSteps + " steps ran");

    }

    void addAgent(Bee bee) {
        agents.add(bee);

        Point startPos = bee.getMovementContext().getPosition();
        board.moveAgent(bee, null, startPos);
        System.out.println("Dodano agenta o ID: " + bee.getID());
    }

    void removeAgent(Bee bee) {
        Point deadPos = bee.getBeePosition();

        if (deadPos != null) {
            board.moveAgent(null, deadPos, null);
        }
        agents.remove(bee);
    }

    public Board getBoard() { return board; }

    public int getForagerCount() {
        int count = 0;
        for (Bee bee : agents) {
            if (bee instanceof Forager) count++;
        }
        return count;
    }

    public Hive getHive() {
        return board.getHive();
    }

    public int getStorerCount() {
        int count = 0;
        for (Bee bee : agents) {
            if (bee instanceof Storer) count++;
        }
        return count;
    }

    private void spawnBees(int count, int age, int defaultX, int defaultY, IBeeCreator creator) {
        for (int i = 0; i < count; i++) {
            int nextId = Bee.getTotalNum();
            Point safePos = findEmptySpawnPosition(board, defaultX, defaultY);
            Bee newBee = creator.create(nextId, age, safePos.x, safePos.y);

            agents.add(newBee);
            board.getCell(safePos.x, safePos.y).setAgent(newBee);
        }
    }

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

enum DeathType {
    STARVATION,
    TRANSFORMATION,
    OLD_AGE
}

