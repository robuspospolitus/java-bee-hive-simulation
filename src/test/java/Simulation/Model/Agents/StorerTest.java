package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.BoardCells.CellType;
import Simulation.Model.SimulationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class StorerTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(32, 16); /**each test has its own board*/
        Bee.resetTotalNumBeesForTesting(); /**making sure total bee counter is intact*/
    }


    @AfterEach
    void tearDown() {
        Bee.resetTotalNumBeesForTesting();/**making sure total bee counter is intact*/
    }

    /**checking if storers' age is increasing properly*/
    @Test
    void testStorer_AgesProperlyOnMove() {
        Storer storer = new Storer(1, 0, 5, 5);
        assertEquals(0, storer.getAge(), "Storer should start at age 0");

        storer.move(board);

        assertEquals(1, storer.getAge(), "Storer age should increase to 1 after moving");
    }

    /**checking if storers' readiness for evolving is assessed correctly*/
    @Test
    void testStorer_IsReadyToEvolve() {
        Storer storer = new Storer(1, 0, 5, 5);

        storer.age = SimulationConfig.TICKS_TO_EVOLVE - 1;
        assertFalse(storer.isReadyToEvolve(), "Should not be ready yet!");

        storer.move(board);

        assertTrue(storer.isReadyToEvolve(), "Should be ready to evolve into a Forager now!");
    }

    /**checking if bees wait in place when trapped */
    @Test
    void testStorer_TrappedByObstacles() {

        Storer storer = new Storer(1, 0, 0, 0);
        board.moveAgent(storer, null, new Point(0, 0));

        board.getCell(0, 1).setType(CellType.OBSTACLE);
        board.getCell(1, 0).setType(CellType.OBSTACLE);
        board.getCell(1, 1).setType(CellType.OBSTACLE);

        storer.move(board);

        assertEquals(new Point(0, 0), storer.getBeePosition(), "Trapped bee should stay exactly where it is");
    }







}