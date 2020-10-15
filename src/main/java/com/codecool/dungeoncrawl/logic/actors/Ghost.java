package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
public class Ghost extends Actor {
    private Cell cell;
    public Ghost(Cell cell) {
        super(cell);
        this.cell = cell;
        this.cell.setActor(this);
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
