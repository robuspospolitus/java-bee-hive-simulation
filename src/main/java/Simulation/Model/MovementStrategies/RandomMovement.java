package Simulation.Model.MovementStrategies;
import Simulation.Model.Board;

import java.awt.Point;
import java.util.Random;

public class RandomMovement implements MovementStrategy {
    private final Random random = new Random();

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

        System.out.println( agentName + " Random move performed, current position [" +position.x + ", " + position.y +"]");
    }
}
