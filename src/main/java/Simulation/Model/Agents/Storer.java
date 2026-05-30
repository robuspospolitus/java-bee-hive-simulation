package Simulation.Model.Agents;

import java.awt.*;

public class Storer extends Bee{
    private Point coordinates;
    int age;

    Queen (int age){
        this.age = age;
        this.coordinates = coordinates;
    }


    protected void move(){
        System.out.println ("Sprzątaczka się porusza");
    }

    protected Point findDestination(){
        Point targetPosition = new Point (1,1);
        System.out.println ("Sprzątaczka znajduje destynację");
        return targetPosition;
    }
}
