package Simulation.Model;

import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Queen;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    @AfterEach
    void tearDown() {
        Bee.resetTotalNumBeesForTesting();
    }

    @Test
    void testIsValidMove_CellsOutOfBounds() {
        Board board = new Board(32, 16);
        assertFalse(board.isValidMove(-1, 5), "Negative x should not be valid");
        assertFalse(board.isValidMove(5, -1), "Negative y should not be valid");
        assertFalse(board.isValidMove(32, 5), "Cell should be out of range on x");
        assertFalse(board.isValidMove(30, 16), "Cell should be out of range on y");

    }

    @Test
    void testIsValidMove_CellTypeOBSTACLE() {
        Board board = new Board(32, 16);
        assertFalse(board.isValidMove(12, 5), "Movement should be blocked on cells with OBSTACLE type");
    }


        @Test
        void testMoveAgent_CleansOldPosition() {
            Board board = new Board(32, 16);

            Bee dummyBee = new Queen(1, 0, 5, 5);

            Point start = new Point(5, 5);
            Point end = new Point(6, 5);

            board.moveAgent(dummyBee, null, start);
            board.moveAgent(dummyBee, start, end);

            assertNull(board.getAgentAt(start.x, start.y), "Old cell should be empty");
            assertEquals(dummyBee, board.getAgentAt(end.x, end.y), "New cell should contain the bee");
        }

}