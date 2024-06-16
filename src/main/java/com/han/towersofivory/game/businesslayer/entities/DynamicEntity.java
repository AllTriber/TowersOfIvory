package com.han.towersofivory.game.businesslayer.entities;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicEntity implements Serializable {

    private String name;
    private Position position;
    private int hp;
    private int attack;
    private int defense;

    protected DynamicEntity() {
        this.hp = 25;
        this.attack = 1;
        this.position = new Position(0, 0, 0);
    }

    public void move(Position position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
        this.position.setZ(position.getZ());
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Get the stats of the entity
     *
     * @return A map containing the stats of the entity
     */
    public Map<String, Integer> getStats() {
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Hitpoints", this.hp);
        stats.put("Attack", this.attack);
        stats.put("Defense", this.defense);
        stats.put("X-cor", this.position.getX());
        stats.put("Y-cor", this.position.getY());
        stats.put("Floor", this.position.getZ());
        return stats;
    }

}
