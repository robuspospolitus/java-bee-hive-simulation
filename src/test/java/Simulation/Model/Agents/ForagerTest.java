package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.BoardCells.CellType;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.MovementStrategies.TeleportMovement;
import Simulation.Model.SimulationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ForagerTest {

    private Board board;

    @BeforeEach
    void setUp() { /**each test has it's own board*/
        board = new Board(32, 16);
    }

    @AfterEach
    void tearDown() {
        Bee.resetTotalNumBeesForTesting();/**making sure total bee counter is intact*/
    }

    //HIVE MOVEMENT


    /**checking if bees target pollen stash when they are carrying it in the hive*/
    @Test
    void testHive_StashesPollen() {

        Forager bee = new Forager(1, 0, 5, 5);
        bee.carriedPollen = 10;

        Point dest = bee.findDestination(board);

        assertEquals(board.getStashDestination(CellType.POLLEN_STASH), dest, "Bee should head to the stash");
        assertTrue(bee.getMovementContext().getStrategy() instanceof TargetedMovement, "Strategy should be set to Targeted");
    }


    /**checking if bees teleport out of the hive correctly*/
    @Test
    void testHive_ExitsWhenFullAndFed() {

        Point exit = board.getHiveExit();
        Forager bee = new Forager(1, 0, exit.x, exit.y);
        bee.carriedPollen = 0;
        bee.setEnergy(SimulationConfig.ENERGY_FULL);

        Point dest = bee.findDestination(board);


        Point expectedTeleport = board.getTeleportDestination(exit);
        assertEquals(expectedTeleport, dest, "Bee should teleport to the hive entrance from hive exit");
        assertTrue(bee.getMovementContext().getStrategy() instanceof TeleportMovement, "Strategy should be set to Teleport");
    }

    /**checking if bees target hive exit when energy full and no pollen is carried*/
    @Test
    void testHive_HeadsToExitWhenEmpty() {

        Forager bee = new Forager(1, 0, 5, 5);
        bee.carriedPollen = 0;
        bee.setEnergy(SimulationConfig.ENERGY_FULL);


        Point dest = bee.findDestination(board);


        assertEquals(board.getHiveExit(), dest, "Bee should walk towards the exit");
    }


    //MEADOW MOVEMENT

    /**checking if bees target hive entrance when full pollen capacity reached*/
    @Test
    void testMeadow_ReturnsWhenPollenIsFull() {

        Forager bee = new Forager(1, 0, 20, 5);
        bee.setEnergy(SimulationConfig.ENERGY_FULL);
        bee.carriedPollen = SimulationConfig.MAX_POLLEN_CAPACITY;

        Point dest = bee.findDestination(board);

        assertEquals(board.getHiveEntrance(), dest, "Full bee should head home");
    }


    /**checking if bees target flowers when within sight radius*/
    @Test
    void testMeadow_FindsClosestFlower() {

        Forager bee = new Forager(1, 0, 20, 5);
        bee.sightRadius = 2;
        bee.carriedPollen = 0;

        board.getCell(21, 6).setFlower(true);
        board.getCell(21, 6).setPollenAmount(5);

        Point dest = bee.findDestination(board);

        assertEquals(new Point(21, 6), dest, "Bee should target the nearby flower");
        assertTrue(bee.getMovementContext().getStrategy() instanceof TargetedMovement);
    }


    /**checking if bees move randomly when no flower target is within sight radius*/
    @Test
    void testMeadow_WandersRandomlyIfNoFlowers() {

        Forager bee = new Forager(1, 0, 20, 5);
        bee.sightRadius = 2;
        bee.carriedPollen = 0;

        Point dest = bee.findDestination(board);

        assertEquals(new Point(20, 5), dest, "Random movement returns the current position");
        assertTrue(bee.getMovementContext().getStrategy() instanceof RandomMovement, "Strategy should be set to Random");
    }
}


