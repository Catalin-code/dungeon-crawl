package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {

    private int health = super.getHealth();

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Skeleton(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "skeleton";
    }
}
