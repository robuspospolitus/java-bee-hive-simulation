package Simulation.Model.Agents;

import java.awt.*;

public class Forager extends Bee {
    int numPollen;
    int carriedPollen;
    int age;
    private Point coordinates;

    Forager (int age) {
        this.age = age;
        this.coordinates;
    }

    protected void move(){
        System.out.println ("Zbieraczka się porusza");
    }

    protected Point findDestination(){
        Point targetPosition = new Point (1,1);
        System.out.println ("Zbieraczka znajduje destynację");
        return targetPosition;
    }
}
