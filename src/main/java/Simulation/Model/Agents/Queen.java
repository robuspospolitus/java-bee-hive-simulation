package Simulation.Model.Agents;
import Simulation.Model.Board;
import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.SimulationConfig;

import java.awt.Point;

public class Queen extends Bee{
    private int eggLayCooldown = 0; // egg laying turn counter

    public Queen (int ID, int age, int spawnX, int spawnY){
        super(ID, age, spawnX, spawnY, null, "Queen");
    }

    @Override
    public void move(Board board){
        age++;
        burnEnergy(SimulationConfig.ENERGY_CONSUMPTION_QUEEN);

        if(eggLayCooldown > 0){
            eggLayCooldown--;
        }
    }

    @Override
    public boolean isTooOld() {
        return false; // The Queen is never "too old"
    }
    @Override
    Point findDestination(Board board){ return spawnPosition; }
    @Override
    public AgentContext getMovementContext(){ return movementContext; }
    @Override
    public Point getBeePosition() { return spawnPosition; }

    public boolean canLayEgg(){
        return eggLayCooldown == 0 && getEnergy() > 30; // how much energy she needs to lay an egg
    }
    public void resetEggCooldown(){
        eggLayCooldown = SimulationConfig.EGG_LAY_COOLDOWN;
    }
    public void receiveFood(){
        setEnergy(SimulationConfig.ENERGY_FULL);
    }
    public Larva layEggs(int newId, int safeX, int safeY){
        System.out.println("The eggs were laid by the queen on " + safeX + ", " + safeY);
        return new Larva(newId, safeX, safeY);
    }
}
