package Simulation.Model.MovementStrategies;

import Simulation.Logger.Logger;
import Simulation.Model.Board;

import java.awt.*;

/**
 * Movement strategy that guides an agent towards a specific target coordinate.
 */
public class TargetedMovement implements MovementStrategy {
    private Point target;

    /**
     * Creates a targeted movement strategy with a defined destination point.
     * @param target the target coordinates to reach
     */
    public TargetedMovement(Point target) {
        this.target = target;
    }

    /**
     * Moves the agent one step closer to the target, handling obstacle avoidance if the path is blocked.
     * @param agentName the identifier name of the agent
     * @param position the current position to modify
     * @param board the simulation board
     */
    @Override
    public void move(String agentName, Point position, Board board) {
        int dx = Integer.compare(target.x, position.x);
        int dy = Integer.compare(target.y, position.y);

        // Straight line
        int nextX = position.x + dx;
        int nextY = position.y + dy;
        if (board.isValidMove(nextX, nextY)) {
            position.x = nextX;
            position.y = nextY;
        } else {
            // Try goinx x-axis
            if (dx != 0 && board.isValidMove(position.x + dx, position.y)) {
                position.x += dx;
            }
            // Try going y-axis
            else if (dy != 0 && board.isValidMove(position.x, position.y + dy)) {
                position.y += dy;
            }
            // Try any other cell
            else {
                int[][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}};
                for (int[] offset : neighbors) {
                    int altX = position.x + offset[0];
                    int altY = position.y + offset[1];
                    if (board.isValidMove(altX, altY)) {
                        position.x = altX;
                        position.y = altY;
                        break;
                    }
                }
            }
        }

        // Borders
        position.x = Math.max(0, Math.min(31, position.x));
        position.y = Math.max(0, Math.min(15, position.y));

        double distance = position.distance(target);
        Logger.log(agentName + " is flying to its destination. Left: " + distance);
    }
}
