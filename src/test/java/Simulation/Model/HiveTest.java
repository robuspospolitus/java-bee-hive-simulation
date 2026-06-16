package Simulation.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiveTest {

    /**ensuring amount of food in hive is decreased accordingly when bees are fed*/
    @Test
    void testConsumeFood_insufficientHoney(){
        Hive hive = new Hive();
        hive.setHoneyAmount(7);
        assertEquals(7, hive.consumeFood(10), "When insufficient food for consumption - available food should be returned");
        hive.setHoneyAmount(7);
        assertEquals(6, hive.consumeFood(6), "When sufficient food for consumption - consumed food should be returned");
    }

}