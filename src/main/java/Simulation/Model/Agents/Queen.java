package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;

import java.awt.Point;

public class Queen extends Bee{
    //private Point position;
    private int eggLayCooldown = 0; //licznik tur do zlozenia jaj
    //private AgentContext movementContext;

    public Queen (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY);
        this.setEnergy(100.0f);
        this.movementContext = new AgentContext("Queen " + ID, new RandomMovement(), new Point(spawnX, spawnY));

    }

    @Override
    public void move(Board board){

    }

    @Override
    protected void interactWithEnvironment(Board board){
        if(eggLayCooldown > 0){
            eggLayCooldown--; //co ture odliczamy do nowej larwy
        }
    }

    public boolean canLayEgg(){
        return eggLayCooldown == 0 && this.getEnergy() > 30; //ile musi miec energi by zmiesc jajo
    }

    public void resetEggCooldown(){
        this.eggLayCooldown = 15;  //co ile nowa larwa jest skladana
    }

    public void receiveFood(){
        this.setEnergy(100.0f);
    }

    @Override
    protected Point findDestination(Board board){
        return this.getBeePosition();
    }

    @Override
    public AgentContext getMovementContext(){
        return this.movementContext;
    }

    protected void layEggs (){
        System.out.println ("Jaja zostały złożone przez królową");
    }


}
