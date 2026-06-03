package Simulation.Model.Agents;

import Simulation.Model.Board;

import java.awt.*;

public class Storer extends Bee{
    private Point coordinates;
    int age;

    public Storer (int age){
        this.age = age;
        this.coordinates = coordinates;
    }


    public void move(Board board){
        System.out.println ("Sprzątaczka się porusza");
    }

    protected Point findDestination(Board board){
        Point targetPosition = new Point (1,1);
        System.out.println ("Sprzątaczka znajduje destynację");
        return targetPosition;
    }
}
