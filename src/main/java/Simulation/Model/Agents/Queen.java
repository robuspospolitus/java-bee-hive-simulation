package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;

import java.awt.Point;

public class Queen extends Bee{
    private Point position;
    private int eggLayCooldown = 0; //licznik tur do zlozenia jaj

    public Queen (int ID, int age, int spawnX, int spawnY){
        this.ID = ID;
        this.age = age;
        this.position = new Point(spawnX, spawnY);
        this.setEnergy(100.0f);
        totalNumBees++;
    }

    @Override
    public void move(Board board){
        this.age++;
        this.setEnergy(this.getEnergy() - 0.5f);

        if(eggLayCooldown > 0){
            eggLayCooldown--;
        }
    }

    public boolean canLayEgg(){
        return eggLayCooldown == 0 && this.getEnergy() > 30; //ile musi miec energi by zmiesc jajo
    }

    public void resetEggColldown(){
        this.eggLayCooldown = 15;  //co ile nowe jajko jest skladane
    }

    public void receiveFood(){
        this.setEnergy(100.0f);
    }

    @Override
    Point findDestination(Board board){
        return this.position;
    }

    @Override
    public AgentContext getMovementContext(){
        return new AgentContext("Queen " + ID, null, this.position);
    }

    protected void layEggs (){
        System.out.println ("Jaja zostały złożone przez królową");
    }


}
