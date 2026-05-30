package Simulation.Model.Agents;

import java.awt.Point;

public class Queen extends Bee{
    int age;
    private Point coordinates;
    Queen (int age){
        this.age = age;
        this.coordinates = coordinates;
    }

protected void move(){
    System.out.println ("Królowa się porusza");
}

protected Point findDestination(){
    Point targetPosition = new Point (1,1);
    System.out.println ("Królowa znajduje destynację");
    return targetPosition;
}

protected void layEggs (){
    System.out.println ("Jaja zostały złożone przez królową");
}


}
