package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.entities.items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Room represents a single room in the dungeon.
 * It holds a 2D array of characters where each character represents a different element of the room, such as a room border or the room itself.
 * It also holds a coordinate which represents the top left corner of the room in the dungeon.
 */
public class Room implements Serializable {
    private int width;
    private int height;
    private Tile tile;
    private List<Tile> doors;
    private char[][] characters;
    private List<Item> items;
    private List<Position> pickedUpItemsPositions;

    /**
     * Constructor for Room.
     *
     * @param width  The width of the room.
     * @param height The height of the room.
     */
    public Room(int width, int height) {
        this.width = width;
        this.height = height;
        this.doors = new ArrayList<>();
        this.characters = new char[height][width];
        this.tile = new Tile(0, 0);
        this.items = new ArrayList<>();
        this.pickedUpItemsPositions = new ArrayList<>();
    }

    /**
     * Generates the room with its borders.
     */
    public void generateRoom() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                fillInRoom(row, col, height, width);
            }
        }
    }

    /**
     * Checks if the room contains the specified tile.
     *
     * @param tile The tile to check.
     * @return True if the tile is within the room, false otherwise.
     */
    public boolean containsTile(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        return x >= this.getX() && x < this.getX() + this.getWidth() &&
                y >= this.getY() && y < this.getY() + this.getHeight();
    }

    /**
     * Fills in the room with appropriate characters for borders and room space.
     *
     * @param row    The current row.
     * @param col    The current column.
     * @param height The height of the room.
     * @param width  The width of the room.
     */
    private void fillInRoom(int row, int col, int height, int width) {
        if (row == 0 && col == 0) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(0);
        } else if (row == 0 && col == width - 1) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(1);
        } else if (row == height - 1 && col == 0) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(2);
        } else if (row == height - 1 && col == width - 1) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(3);
        } else if (row == 0 || row == height - 1) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(4);
        } else if (col == 0 || col == width - 1) {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(5);
        } else {
            characters[row][col] = ASCIIInterfaceCharacters.ROOM.getCharacter();
        }
    }

    /**
     * Adds a door to the room.
     *
     * @param x The x-coordinate of the door.
     * @param y The y-coordinate of the door.
     */
    public void addDoor(int x, int y) {
        this.doors.add(new Tile(x, y));
    }

    /**
     * Gets the list of doors in the room.
     *
     * @return The list of doors.
     */
    public List<Tile> getDoors() {
        return doors;
    }

    /**
     * Adds a door to the room.
     *
     * @param tile The door to add.
     */
    public void addDoor(Tile tile) {
        doors.add(tile);
    }

    /**
     * Gets the x-coordinate of the top left corner of the room.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return tile.getX();
    }

    /**
     * Sets the x-coordinate of the top left corner of the room.
     *
     * @param x The x-coordinate to set.
     */
    public void setX(int x) {
        tile.setX(x);
    }

    /**
     * Gets the y-coordinate of the top left corner of the room.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return tile.getY();
    }

    /**
     * Sets the y-coordinate of the top left corner of the room.
     *
     * @param y The y-coordinate to set.
     */
    public void setY(int y) {
        tile.setY(y);
    }

    /**
     * Gets the width of the room.
     *
     * @return The width of the room.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the room.
     *
     * @return The height of the room.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the character at a specific coordinate in the room.
     *
     * @param y The y-coordinate.
     * @param x The x-coordinate.
     * @return The character at the specified coordinate.
     */
    public char getCharacter(int y, int x) {
        return characters[y][x];
    }

    /**
     * Gets the items in the room.
     *
     * @return The items in the room.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Adds an item to the room.
     *
     * @param item The item to add.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the room.
     */
    public void clearItems() {
        items.clear();
    }

    /**
     * Adds item to pickedUpItems
     * @param position
     */
    public void addPickedUpItemPosition(Position position) {
        pickedUpItemsPositions.add(position);
    }

    /**
     * Returns the picked up items
     * @return Picked up items
     */
    public List<Position> getPickedUpItemsPositions() {
        return pickedUpItemsPositions;
    }
}
