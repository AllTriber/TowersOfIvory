package com.han.towersofivory.game.businesslayer.entities.items;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;

import java.io.Serializable;

/**
 * Represents an abstract item.
 */
public abstract class Item implements Serializable {
    protected char character;
    protected Position position = new Position(0, 0, 0);

    /**
     * Adds an item to a player.
     *
     * @param player The player to add the item to.
     */
    public abstract void addItem(Player player);

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Gets the character representation of the item.
     *
     * @return The character representation of the item.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Sets the position of the item.
     *
     * @param position The position to set.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the position of the item.
     *
     * @return The position of the item.
     */
    public Position getPosition() {
        return position;
    }
}
