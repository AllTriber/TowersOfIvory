package com.han.towersofivory.game.businesslayer.entities;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class PlayerVision implements Serializable {
    private int hp;
    private int atk;
    private int def;
    private int maxHp;
    private Room room;
    private boolean inRoom;
    private String lastMovement;
    private int x;
    private int y;
    private int z;
    private HashMap<String, Integer> objectsInRoom;
    private HashMap<UUID, Position> otherPlayersInRoom;


    public PlayerVision(Room room, boolean inRoom, String lastMovement) {
        this.room = room;
        this.inRoom = inRoom;
        this.lastMovement = lastMovement;
        this.objectsInRoom = new HashMap<>();
        this.objectsInRoom.put("items", 0);
        this.objectsInRoom.put("doors", 0);
        this.objectsInRoom.put("stairs", 0);
        this.objectsInRoom.put("otherPlayers", 0);
        this.otherPlayersInRoom = new HashMap<>();
    }


    public boolean getInRoom() {
        return inRoom;
    }

    public void setInRoom(boolean inRoom) {
        this.inRoom = inRoom;
    }

    public Room getRoom() {
        return room;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public String getLastMovement() {
        return lastMovement;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


    public void setZ(int z) {
        this.z = z;
    }

    public void setLastMovement(String command) {
        this.lastMovement = command;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @SuppressWarnings("squid:S1319")
    public HashMap<String, Integer> getObjectsInRoom() {
        return objectsInRoom;
    }

    @SuppressWarnings("squid:S1319")
    public HashMap<UUID, Position> getOtherPlayersInRoom() {
        return otherPlayersInRoom;
    }

}

