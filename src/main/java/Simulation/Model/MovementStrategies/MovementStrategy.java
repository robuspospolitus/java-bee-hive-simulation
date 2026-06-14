package Simulation.Model.MovementStrategies;

import java.awt.*;
import Simulation.Model.Board;

public interface MovementStrategy {
    void move(String agentName, Point position, Board board);
}

