package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement; // potrzebne tylko jako placeholder
import java.awt.Point;

public class Larva extends Bee {

    public Larva (int ID, int age, int startX, int startY){
        super(ID, age, startX, startY);
        this.movementContext = new AgentContext("Larva " + ID, new RandomMovement(), new Point(startX, startY));
    }

    @Override
    public void move(Board board){

    }

    @Override
    protected Point findDestination(Board board){
        return this.getBeePosition();
    }

    public void feed(){
        this.setEnergy(100.0f);
        System.out.println("Larva "+ getID() + " nakarmiona");
    }
}
