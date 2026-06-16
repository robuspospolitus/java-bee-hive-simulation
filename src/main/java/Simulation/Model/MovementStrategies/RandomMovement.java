package Simulation.Model.MovementStrategies;
import Simulation.Logger.Logger;
import Simulation.Model.Board;

import java.awt.Point;
import java.util.Random;

/**
 * Movement strategy that moves an agent to a random adjacent cell.
 */
public class RandomMovement implements MovementStrategy {
    private final Random random = new Random();

    /**
     * Calculates and applies a random valid move for the agent.
     * @param agentName the identifier name of the agent
     * @param position the current position to modify
     * @param board the simulation board
     */
    @Override
    public void move(String agentName, Point position, Board board){
        int attempts = 0;
        while (attempts < 10) {
            int nextX = position.x + random.nextInt(3) - 1;
            int nextY = position.y + random.nextInt(3) - 1;

            if (board.isValidMove(nextX, nextY)) {
                position.x = nextX;
                position.y = nextY;
                break;
            }
            attempts++;
        }

        position.x = Math.max(0, Math.min(31, position.x));
        position.y = Math.max(0, Math.min(15, position.y));

        Logger.log( agentName + " random move performed, current position [" +position.x + ", " + position.y +"]");
    }
}
