package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Octopus;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;
    private Skeleton skeleton;
    private Octopus octopus;
    private Ghost ghost;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setSkeleton(Skeleton skeleton){
        this.skeleton = skeleton;
    }
    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void setOctopus(Octopus octopus){
        this.octopus = octopus;
    }
    public Octopus getOctopus(){ return octopus; }

    public void setGhost(Ghost ghost){this.ghost = ghost;}
    public Ghost getGhost(){return ghost;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
