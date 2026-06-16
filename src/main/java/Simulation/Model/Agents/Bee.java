package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.BoardCells.Cell;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.MovementStrategy;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public abstract class Bee {
    protected int carryCapacity, carriedPollen;
    protected float energy;
    protected int sightRadius;
    static int totalNumBees;
    protected int ID;
    protected int age;
    protected AgentContext movementContext;
    protected Point spawnPosition;

    /**
     * Initializes a base Bee agent with its unique identifiers, initial age, spawning coordinates, and movement strategy.
     * @param ID the unique identifier assigned to this specific bee
     * @param age the starting age of the bee in simulation ticks
     * @param x the initial X-coordinate on the simulation board
     * @param y the initial Y-coordinate on the simulation board
     * @param strategy the movement behavior pattern assigned to the bee
     * @param prefix the text prefix used to label the agent's context configuration
     */
    public Bee(int ID, int age, int x, int y, MovementStrategy strategy, String prefix) {
        this.ID = ID;
        this.age = age;
        spawnPosition = new Point(x, y);
        movementContext = new AgentContext(prefix + " " + ID, strategy, spawnPosition);
        energy = SimulationConfig.ENERGY_FULL;
        carryCapacity = SimulationConfig.MAX_POLLEN_CAPACITY;
        totalNumBees++;
    }

    /**
     * Decreases the bee's current energy level by the specified amount, preventing it from dropping below zero.
     * @param amount the quantity of energy to consume during an action or tick
     */
    public void burnEnergy(float amount){
        energy -= amount;
        if(energy < 0){
            energy = 0;
        }
    }

    /**
     * Determines whether the bee has depleted all of its energy resources.
     * @return true if the energy level is zero or less, false otherwise
     */
    public boolean isDead(){
        return energy <= 0;
    }

    /**
     * Checks if the bee's age has exceeded the maximum allowable lifespan configured for the simulation.
     * @return true if the bee is older than the maximum permitted age, false otherwise
     */
    public boolean isTooOld() {
        return age > SimulationConfig.MAX_BEE_AGE;
    }

    /**
     * Retrieves the current spatial position coordinates of the bee from its movement context.
     * @return a Point object representing the current coordinates, or null if the context is missing
     */
    public Point getBeePosition() {
        if (getMovementContext() != null) {
            return getMovementContext().getPosition();
        }
        return null;
    }

    /**
     * Updates the bee's location on the simulation board based on its internal navigation logic.
     * @param board the simulation board containing environment and cell data
     */
    public abstract void move(Board board);

    /**
     * Calculates and identifies the target coordinate destination for the bee to head toward.
     * @param board the simulation board used to scan for potential targets in the environment
     * @return a Point representing the target destination coordinates
     */
    abstract Point findDestination(Board board);

    /**
     * Retrieves the unique identification number of the bee.
     * @return the unique ID integer of this bee instance
     */
    public int getID (){
        return ID;
    }

    /**
     * Retrieves the global counter tracking the total number of bee objects instantiated during the simulation lifecycle.
     * @return the total count of created bees
     */
    public static int getTotalNum(){
        return totalNumBees;
    }

    /**
     * Retrieves the strategy context managing the agent's spatial state and navigation logic.
     * @return the AgentContext instance associated with the bee, or null in this base implementation
     */
    public AgentContext getMovementContext() {
        return null;
    }

    /**
     * Retrieves the current remaining energy level of the bee.
     * @return the remaining energy value
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Updates the bee's energy to a specific value.
     * @param energy the new energy value to assign to the bee
     */
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    /**
     * Retrieves the current age of the bee measured in simulation steps.
     * @return the current age of the bee
     */
    public int getAge() {
        return age;
    }

    /**
     * Performs an interaction between the bee and the environmental board cell it is currently occupying.
     * @param cell the board cell with which the bee is interacting
     */
    public void interact(Cell cell) { }
}
