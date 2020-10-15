package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
public class Ghost extends Actor {
    private Cell cell;
    public Ghost(Cell cell) {
        super(cell);
        this.cell = cell;
        this.cell.setActor(this);
    }

//    @Override
//    public void move(int dx, int dy){
//        boolean move = true;
//        Cell nextCell = cell.getNeighbor(dx, dy);
//        if (nextCell.getActor() != null){
//            move = false;
//        }
//
//        if (move){
//            cell.setActor(null);
//            nextCell.setActor(this);
//            cell = nextCell;
//        }
//    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
