package Simulation.Model.MovementStrategies;
import java.awt.Point;
import java.util.Random;

public class RandomMovement implements MovementStrategy {
    private final Random random = new Random();

    @Override
    public void move(String agentName, Point position){
        position.x +=random.nextInt(3) - 1;
        position.y += random.nextInt(3) -1;

        position.x = Math.max(0, Math.min(31, position.x));
        position.y = Math.max(0, Math.min(15, position.y));

       System.out.println( agentName + "Random move performed, current position [" +position.x + ", " + position.y +" ]");
    }
}
