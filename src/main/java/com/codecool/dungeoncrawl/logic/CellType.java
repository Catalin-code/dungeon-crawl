package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    DOOR("door"),
    CDOOR("cdoor"),
    EXIT("exit"),
    TREE("tree"),
    WATER("water"),
    BRIDGE("bridge"),
    FINISH("finish"),
    WALL("wall"),
    GRASS("grass");


    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
