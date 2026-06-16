package Simulation.Model.Agents;

import Simulation.Model.Board;
import Simulation.Model.SimulationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BeeTest {

    /**creating an example bee, because no instance of an abstract class can be created*/
    static class DummyBee extends Bee {
        public DummyBee(int ID, int age, int x, int y) {

            super(ID, age, x, y, null, "Dummy");
        }

        @Override
        public void move(Board board) {} // unnecessary for testing

        @Override
        Point findDestination(Board board) { return null; } // unnecessary for testing
    }

    @AfterEach
    void tearDown() {
        Bee.resetTotalNumBeesForTesting(); //making sure total number of bees stays intact outside of testing
    }


    /**ensuring that energy drops no lower than 0*/
    @Test
    void testBurnEnergy_StopsAtZero() {
        DummyBee bee = new DummyBee(1, 0, 5, 5);

        bee.setEnergy(10.0f);

        bee.burnEnergy(4.0f);
        assertEquals(6.0f, bee.getEnergy(), "Energy should decrease by the set number (in this case 4)");

        bee.burnEnergy(20.0f);
        assertEquals(0.0f, bee.getEnergy(), "Energy should never drop below 0");
    }

    /**checking if bees die only when their energy < 0*/
    @Test
    void testIsDead_TriggeredByZeroEnergy() {
        DummyBee bee = new DummyBee(1, 0, 5, 5);

        bee.setEnergy(5.0f);
        assertFalse(bee.isDead(), "Bee should be alive when energy above 0");

        bee.setEnergy(0.0f);
        assertTrue(bee.isDead(), "Bee should be dead when energy is 0");

        bee.setEnergy(-5.0f);
        assertTrue(bee.isDead(), "Bee should be dead when energy is below 0");
    }

    /**checking edgecase of a bee being old and too old*/
    @Test
    void testIsTooOld() {
        int maxAge = SimulationConfig.MAX_BEE_AGE;
        DummyBee bee = new DummyBee(1, maxAge, 5, 5);

        assertFalse(bee.isTooOld(), "Bee is at max age, but not any older than that yet");


        DummyBee oldBee = new DummyBee(2, maxAge + 1, 5, 5);
        assertTrue(oldBee.isTooOld(), "Bee should be considered too old now");
    }

    /**checking if bee counter increases value of totalNumBees correctly*/
    @Test
    void testTotalNumBees_CounterIncreases() {
        assertEquals(0, Bee.getTotalNum());

        new DummyBee(1, 0, 0, 0);
        assertEquals(1, Bee.getTotalNum(), "Only one bee instance exists at this point");

        new DummyBee(2, 0, 0, 0);
        new DummyBee(3, 0, 0, 0);
        assertEquals(3, Bee.getTotalNum(), "Correct count is now 3 bees");
    }

}