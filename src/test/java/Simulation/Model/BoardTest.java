package Simulation.Model;

import Simulation.Model.Agents.Bee;
import Simulation.Model.Agents.Queen;
import Simulation.Model.BoardCells.CellType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    @AfterEach
    void tearDown() {
        Bee.resetTotalNumBeesForTesting(); //ensuring bee counter stays intact
    }

    /**ensuring movement is assessed as invalid when cell has coords out of bounds*/
    @Test
    void testIsValidMove_CellsOutOfBounds() {
        Board board = new Board(32, 16);
        assertFalse(board.isValidMove(-1, 5), "Negative x should not be valid");
        assertFalse(board.isValidMove(5, -1), "Negative y should not be valid");
        assertFalse(board.isValidMove(32, 5), "Cell should be out of range on x");
        assertFalse(board.isValidMove(30, 16), "Cell should be out of range on y");

    }
    /**ensuring movement is assessed as invalid when cell is of type OBSTAClE*/
    @Test
    void testIsValidMove_CellTypeOBSTACLE() {
        Board board = new Board(32, 16);
        assertFalse(board.isValidMove(12, 5), "Movement should be blocked on cells with OBSTACLE type");
    }

    /**testing clearing old and appearing of a bee in a new cell on board when movement is successful*/
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
    /**checking if correct stashes in hive are found and if the method is secure when invalid CellType is passed*/
    @Test
    void testGetStashDestination_incorrectCoords(){
        Board board = new Board(32, 16);
        assertNull(board.getStashDestination(CellType.OBSTACLE), "If invalid cell type parameter passed - function should return null");
        assertEquals(new Point (0,0), board.getStashDestination(CellType.POLLEN_STASH), "Searching for pollen stash should return point with coords (0,0)");
        assertEquals(new Point (0,1), board.getStashDestination(CellType.HONEY_STASH), "Searching for honey stash should return point with coords (0,1)");
    }


}