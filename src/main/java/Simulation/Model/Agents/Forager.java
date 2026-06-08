package Simulation.Model.Agents;

import Simulation.Model.MovementStrategies.AgentContext;
import Simulation.Model.MovementStrategies.RandomMovement;
import Simulation.Model.MovementStrategies.TargetedMovement;
import Simulation.Model.Board;
import Simulation.Model.Hive;
import Simulation.Model.BoardCells.Cell;

import java.awt.*;

public class Forager extends Bee {
    int numPollen;
    int carriedPollen;
    int spawnX;
    int spawnY;
    Point spawnPosition;

    protected int sightRadius = 3;
    private AgentContext movementContext;

    public Forager(int ID, int age,int spawnX, int spawnY) {
        this.ID = ID;
        this.age = age;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnPosition = new Point(spawnX,spawnY);
        this.movementContext = new AgentContext("Forager"+ID, new RandomMovement(), this.spawnPosition);
        totalNumBees++;
    }

    public void collectPollen(int amount) {
        this.carriedPollen += amount;
        System.out.println("Zbieraczka " + ID + " zebrala pylek. Posiada teraz: " + carriedPollen);
    }

    @Override
    public void move(Board board) {
        findDestination(board);
        Point oldPos = new Point(movementContext.getPosition());
        movementContext.performMove();
        Point newPos = movementContext.getPosition();

        board.moveAgent(this, oldPos, newPos);
        System.out.println("Zbieraczka się porusza");

        this.age++;
        this.burnEnergy(1.0f);

        if (newPos.x == 0 && newPos.y == 0 && this.carriedPollen > 0) { //wspolrzedne ula
            Hive ul = board.getHive();

            //przekazujemy cały zebrany pyłek do ula
            ul.setPollenAmount(ul.getPollenAmount() + this.carriedPollen);
            System.out.println(" Zbieraczka " + ID + " rozładowała " + this.carriedPollen + " szt. pyłku do ula!");
            this.carriedPollen = 0;

            //Skoro jest w ulu to je
            if (ul.getFoodAmount() > 0) {
                ul.setFoodAmount(ul.getFoodAmount() - 1); // zjada jedzenie ula
                this.setEnergy(100);
                System.out.println(" Zbieraczka " + ID + " zjadła");
            } else {
                System.out.println(" Zbieraczka " + ID + " jest w ulu, ale glodna");
            }
        }
    }

    protected Point findDestination(Board board) {
        if(this.getEnergy() < 25.0f || carriedPollen >= 10){
            System.out.println("zbieraczka " + ID + " wraca do ula");
            movementContext.setStrategy(new TargetedMovement(new Point (0,0))); //wspolrzedne ula
            return new Point(0,0);
        }

        Point currentPos = this.movementContext.getPosition();
        Cell[][] grid = board.getGrid();

        for (int x = -sightRadius; x <= sightRadius; x++) {
            for (int y = -sightRadius; y <= sightRadius; y++) {
                int checkX = currentPos.x + x;
                int checkY = currentPos.y + y;

                if (checkX >= 0 && checkX < grid.length && checkY >= 0 && checkY < grid[0].length) {
                    if (grid[checkX][checkY] != null && !grid[checkX][checkY].isEmpty()) {
                        System.out.println("I'm seeing a flower");
                        movementContext.setStrategy(new TargetedMovement(new Point(checkX, checkY)));
                        return new Point(checkX, checkY);
                    }
                }
            }
        }


        System.out.println("I don't see anything, flying random...");
        movementContext.setStrategy(new RandomMovement());
        return new Point(1, 1);
    }

    public AgentContext getMovementContext() { return this.movementContext; }

}