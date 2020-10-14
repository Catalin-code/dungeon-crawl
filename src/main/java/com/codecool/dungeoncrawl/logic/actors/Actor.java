package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health = 100;
    private List<String> inventory = new ArrayList<>();

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void move(int dx, int dy) {
        boolean move = true;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType().equals(CellType.WALL)){
            move = false;
        }

        if (nextCell.getType().equals(CellType.DOOR)){
            if(inventory.contains("Key")){
                move = true;
                nextCell.setType(CellType.CDOOR);
            } else { move = false; }
        }

        if (nextCell.getActor() != null){
            move = false;

        }

        if (move){
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
