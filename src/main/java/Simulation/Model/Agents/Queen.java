package Simulation.Model.Agents;

import Simulation.Model.Board;

import java.awt.Point;

public class Queen extends Bee{
    int age;
    private Point coordinates;

    Queen (int id, int age){
        this.age = age;
        this.coordinates = coordinates;
    }

public void move(Board board){
    System.out.println ("Królowa się porusza");
}

protected Point findDestination(Board board){
    Point targetPosition = new Point (1,1);
    System.out.println ("Królowa znajduje destynację");
    return targetPosition;
}

protected void layEggs (){
    System.out.println ("Jaja zostały złożone przez królową");
}


}
