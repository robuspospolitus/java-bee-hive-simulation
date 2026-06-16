package Simulation.Model.Agents;

public interface IBeeCreator {
    /**
     * Creates a new bee instance with the specified parameters.
     * @param id unique identifier for the bee
     * @param age initial age of the bee
     * @param x initial X coordinate on the board
     * @param y initial Y coordinate on the board
     * @return the newly created bee object
     */
    Bee create(int id, int age, int x, int y);
}
