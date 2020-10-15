package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Player extends Actor {
    private List<String> inventory = getInventory();

    @Override
    public void setInventory(List<String> inventory) {
        super.setInventory(inventory);
    }

    @Override
    public List<String> getInventory() {
        return super.getInventory();
    }


    private int health = super.getHealth();

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Player(Cell cell) {
        super(cell);
    }

    public void addToInventory(String item){
        inventory.add(item);
        setInventory(inventory);
    }

    public String getTileName() {
        return "player";
    }
}
