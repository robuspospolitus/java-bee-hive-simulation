package Simulation.Model.MovementStrategies;

import Simulation.Model.Board;

import java.awt.*;

public class TargetedMovement implements MovementStrategy {
    private Point target;

    public TargetedMovement(Point target) {
        this.target = target;
    }

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
        System.out.println(agentName + " leci do celu. Zostało: " + distance);
    }
}
