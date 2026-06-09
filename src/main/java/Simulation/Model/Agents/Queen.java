package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public class Queen extends Bee{
    private Point position;
    private int eggLayCooldown = 0; //licznik tur do zlozenia jaj
    private AgentContext movementContext;

    public Queen (int ID, int age, int spawnX, int spawnY){
        this.ID = ID;
        this.age = age;
        this.position = new Point(spawnX, spawnY);
        this.setEnergy(SimulationConfig.ENERGY_FULL);
        this.movementContext = new AgentContext("Queen " + ID, null, this.position);

        totalNumBees++;
    }

    @Override
    public void move(Board board){
        this.age++;
        this.burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_QUEEN);

        if(eggLayCooldown > 0){
            eggLayCooldown--;
        }
    }

    public boolean canLayEgg(){
        return eggLayCooldown == 0 && this.getEnergy() > 30; //ile musi miec energi by zmiesc jajo
    }

    public void resetEggCooldown(){
        this.eggLayCooldown = SimulationConfig.EGG_LAY_COOLDOWN;
    }

    public void receiveFood(){
        this.setEnergy(SimulationConfig.ENERGY_FULL);
    }

    @Override
    Point findDestination(Board board){
        return this.position;
    }

    @Override
    public boolean isDead() {
        // The Queen never dies! (At least for now)
        return false;
    }

    @Override
    public AgentContext getMovementContext(){
        return this.movementContext;
    }

    public Larva layEggs(int newId, int safeX, int safeY){
        System.out.println("Jaja zostały złożone przez królową na " + safeX + ", " + safeY);
        return new Larva(newId, safeX, safeY);
    }

    @Override
    public Point getBeePosition() {
        // Force the engine to always use her exact throne coordinates
        return this.position;
    }


}
