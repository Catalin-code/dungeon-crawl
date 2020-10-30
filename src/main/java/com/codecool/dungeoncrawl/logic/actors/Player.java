package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Player extends Actor {
    private String name;
    private List<String> inventory = getInventory();
    private boolean sword;
    private boolean keys;
    private boolean door;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSword() {
        return sword;
    }

    public void setSword(boolean sword) {
        this.sword = sword;
    }

    public boolean isKeys() {
        return keys;
    }

    public void setKeys(boolean keys) {
        this.keys = keys;
    }

    public boolean isDoor() {
        return door;
    }

    public void setDoor(boolean door) {
        this.door = door;
    }


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

    @Override
    public String getTileName() {
        return "player";
    }



    public void setHealth(int health) {
        this.health = health;
    }

    public Player(Cell cell) {
        super(cell);
    }

    public void addToInventory(String item) {
        inventory.add(item);
        setInventory(inventory);
    }
}
