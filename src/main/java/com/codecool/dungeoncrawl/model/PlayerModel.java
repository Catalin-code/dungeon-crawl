package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.List;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int hp;
    private int x;
    private int y;
    private List<String> inventory;
    private boolean sword;
    private boolean keys;
    private boolean door;

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

    public PlayerModel(String playerName, int hp, int x, int y, boolean sword, boolean keys, boolean door) {
        this.playerName = playerName;
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.sword = sword;
        this.keys = keys;
        this.door = door;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getName();
        this.x = player.getX();
        this.y = player.getY();

        this.hp = player.getHealth();
        this.inventory = player.getInventory();

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
