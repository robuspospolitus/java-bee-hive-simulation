package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.SimulationConfig;
import java.awt.Point;

public class Larva extends Bee {
    private Point position;
    private int feedingsRequired = SimulationConfig.LARVA_FEEDINGS_NEEDED;
    private int feedingsReceived = 0;
    private AgentContext movementContext;

    public Larva(int ID, int spawnX, int spawnY) {
        this.ID = ID;
        this.age = 0;
        this.position = new Point(spawnX, spawnY);
        this.movementContext = new AgentContext("Larva " + ID, null, this.position);
        totalNumBees++;
    }

    @Override
    public void move(Board board) {
        this.age++;
    }

    public void beFed() {
        this.feedingsReceived++;
    }

    public boolean isReadyToTransform() {
        return this.feedingsReceived >= this.feedingsRequired;
    }

    @Override
    Point findDestination(Board board) {
        return this.position;
    }

    @Override
    public AgentContext getMovementContext() {
        return this.movementContext;
    }

    public Point getPosition() {
        return this.position;
    }
    @Override
    public boolean isDead() {
        // Larvae don't die of starvation the same way adults do!
        return false;
    }
}