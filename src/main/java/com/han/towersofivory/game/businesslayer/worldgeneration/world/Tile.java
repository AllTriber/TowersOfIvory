package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import java.io.Serializable;

/**
 * Coordinate represents a point in the dungeon with x and y coordinates.
 * It is used to specify the location of rooms, corridors, doors, and other elements in the dungeon.
 */
public class Tile implements Serializable {
    private int x;
    private int y;

    /**
     * Constructor for Coordinate.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate.
     *
     * @param x The x-coordinate to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate.
     *
     * @param y The y-coordinate to set.
     */
    public void setY(int y) {
        this.y = y;
    }
}
