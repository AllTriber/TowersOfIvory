package com.han.towersofivory.game.businesslayer.entities;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    int x;
    int y;
    int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Position getAdjustedPosition(int x, int y, int z) {
        return new Position(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public void adjustY(int yAdjustment) {
        this.y += yAdjustment;
    }

    public void adjustX(int xAdjustment) {
        this.x += xAdjustment;
    }

    /**
     * UC 2 Spelen Spel -> US 2.4 Chatfunctie
     *
     * returns the distance between this and the other Position
     * if the z is different, the distance is 1000
     * There is no diagonal movement, so the distance is the maximum of the difference in x and y
     * Example: distanceTo(1, 1, 0) and distanceTo(2, 2, 0) returns 1
     *
     * @param other - the position to compare to.
     * @return the distance
     */
    public int distanceTo(Position other) {
        if (z != other.z)
            return 1000;
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }
}
