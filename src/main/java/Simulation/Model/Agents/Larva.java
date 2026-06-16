package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.SimulationConfig;
import java.awt.Point;

public class Larva extends Bee {
    private int feedingsReceived = 0;

    /**
     * Creates a new Larva agent at the specified spawn coordinates with zero age.
     * @param ID unique identifier of the larva
     * @param spawnX starting X coordinate
     * @param spawnY starting Y coordinate
     */
    public Larva(int ID, int spawnX, int spawnY) {
        super(ID, 0, spawnX, spawnY, null, "Larva");
    }

    @Override
    public void move(Board board) { age++; }
    @Override
    Point findDestination(Board board) { return spawnPosition; }
    @Override
    public AgentContext getMovementContext() { return movementContext; }
    @Override
    public boolean isDead() {
        // Larvae don't die of starvation the same way adults do!
        return false;
    }

    /**
     * Increments the total counter of feedings this larva has received.
     */
    public void beFed() { feedingsReceived++; }
    /**
     * Checks if the larva has received the required amount of food to evolve.
     * @return true if the feeding threshold is met, false otherwise
     */
    public boolean isReadyToTransform() {
        int feedingsRequired = SimulationConfig.LARVA_FEEDINGS_NEEDED;
        return feedingsReceived >= feedingsRequired; }
}