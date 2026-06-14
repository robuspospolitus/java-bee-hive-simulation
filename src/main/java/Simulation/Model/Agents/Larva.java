package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.SimulationConfig;
import java.awt.Point;

public class Larva extends Bee {
    private int feedingsRequired = SimulationConfig.LARVA_FEEDINGS_NEEDED;
    private int feedingsReceived = 0;

    public Larva(int ID, int spawnX, int spawnY) {
        super(ID, 0, spawnX, spawnY, null, "Larwa");
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

    public void beFed() { feedingsReceived++; }
    public boolean isReadyToTransform() { return feedingsReceived >= feedingsRequired; }
    public Point getPosition() { return spawnPosition; }
}