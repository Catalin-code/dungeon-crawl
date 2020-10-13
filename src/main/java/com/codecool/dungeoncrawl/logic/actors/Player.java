package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {

    private List<String> inventory = new ArrayList<>();

    public Player(Cell cell) {
        super(cell);
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void addToInventory(String item){
        inventory.add(item);
    }

    public String getTileName() {
        return "player";
    }
}
