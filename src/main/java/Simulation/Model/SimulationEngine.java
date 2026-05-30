package Simulation.Model;
import Simulation.Model.Agents.Bee;

public class SimulationEngine {

    //SimulationEngine(int width, int height, int numWorkers, int numFlowers){
   // }

   // SimulationEngine sim = new SimulationEngine(int width, int height, int numWorkers,int numFlowers);

void initialize(){
    System.out.println("Simulation initialized");
}

int steps(){
    System.out.println("Steps ran");
    return 0;
}

void run(int steps){
    System.out.println("Steps ran");
}

void addAgent(Bee bee){
    System.out.println("Dodano agenta");
}

void removeAgent (Bee bee){
    System.out.println("Usunięto agenta");
}

}
