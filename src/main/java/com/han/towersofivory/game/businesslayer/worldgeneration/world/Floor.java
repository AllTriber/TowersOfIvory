package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.entities.items.Item;

import java.io.Serializable;
import java.util.*;

/**
 * Floor represents the entire dungeon layout.
 * It holds a 2D array of characters where each character represents a different element of the dungeon, such as a room, corridor, or door.
 */
public class Floor implements Serializable {
    private int width;
    private int height;
    private List<Room> rooms;
    private char[][] characters;
    private ArrayList<Room> inaccessibleRooms;
    private long seed;
    private int level;

    /**
     * Constructor for Floor.
     *
     * @param width  The width of the floor.
     * @param height The height of the floor.
     */
    public Floor(int width, int height) {
        this.width = width;
        this.height = height;
        this.characters = new char[height][width];
        this.rooms = new ArrayList<>();
        this.inaccessibleRooms = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                characters[row][col] = ASCIIInterfaceCharacters.SPACE.getCharacter();
            }
        }
    }

    /**
     * Checks if the tile at the specified coordinates is walkable.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return True if the tile is walkable, false otherwise.
     */
    public boolean isWalkable(int x, int y) {
        char character = getAsciiCharacterOfTile(new Tile(x, y));
        char[] walkableCharacters = ASCIIInterfaceCharacters.getWalkableCharacters();
        for (char walkableCharacter : walkableCharacters) {
            if (character == walkableCharacter) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes rooms that contain inaccessible tiles.
     *
     * @param inaccessibleTiles The list of inaccessible tiles.
     */
    public void removeInaccessibleRooms(List<Tile> inaccessibleTiles) {
        Set<Room> roomsToRemove = new HashSet<>();
        for (Tile tile : inaccessibleTiles) {
            for (Room room : rooms) {
                if (room.containsTile(tile)) {
                    roomsToRemove.add(room);
                    inaccessibleRooms.add(room);
                    break;
                }
            }
        }
        rooms.removeAll(roomsToRemove);
    }

    /**
     * Gets the list of inaccessible rooms.
     *
     * @return The list of inaccessible rooms.
     */
    public List<Room> getInaccessibleRooms() {
        return inaccessibleRooms;
    }

    /**
     * Gets the width of the floor.
     *
     * @return The width of the floor.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the floor.
     *
     * @return The height of the floor.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the character at a specific tile in the floor.
     *
     * @param tile The tile to get the character from.
     * @return The character at the specified tile.
     */
    public char getAsciiCharacterOfTile(Tile tile) {
        if (tile.getX() < 0 || tile.getX() >= width || tile.getY() < 0 || tile.getY() >= height) {
            return ASCIIInterfaceCharacters.SPACE.getCharacter();
        }
        return characters[tile.getY()][tile.getX()];
    }

    /**
     * Sets the character at a specific coordinate in the floor.
     *
     * @param y The y-coordinate.
     * @param x The x-coordinate.
     * @param character The character to set.
     */
    public void setCharacter(int y, int x, char character) {
        characters[y][x] = character;
    }

    /**
     * Returns a string representation of the floor.
     *
     * @return The string representation of the floor.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                sb.append(characters[row][col]);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Gets the direction of the stairs at a specific tile.
     *
     * @param tile The tile to check for stairs.
     * @return 1 if the stairs are going up, -1 if the stairs are going down, 0 otherwise.
     */
    public int getStairsDirection(Tile tile) {
        if (characters[tile.getY()][tile.getX()] == ASCIIInterfaceCharacters.STAIRS.getCharacter(0)) {
            return 1;
        } else if (characters[tile.getY()][tile.getX()] == ASCIIInterfaceCharacters.STAIRS.getCharacter(1)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Gets the tile of the stairs in the specified direction.
     *
     * @param stairsDirection The direction of the stairs (1 for up, -1 for down).
     * @return The tile of the stairs, or null if no stairs are found.
     */
    public Tile getStairsTile(int stairsDirection) {
        char stairs;
        switch (stairsDirection) {
            case 1 -> stairs = ASCIIInterfaceCharacters.STAIRS.getCharacter(0);
            case -1 -> stairs = ASCIIInterfaceCharacters.STAIRS.getCharacter(1);
            default -> {
                return null; // Invalid stairs direction
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (getAsciiCharacterOfTile(new Tile(x, y)) == stairs) {
                    return new Tile(x, y);
                }
            }
        }

        return null; // No stairs found
    }

    public List<Tile> getCorridorTiles() {
        List<Tile> corridorTiles = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Tile tile = new Tile(x, y);
                if (getAsciiCharacterOfTile(tile) == ASCIIInterfaceCharacters.CORRIDOR.getCharacter()) {
                    corridorTiles.add(tile);
                }
            }
        }
        return corridorTiles;
    }


    /**
     * Adds a room to the floor.
     *
     * @param room The room to add.
     */

    public void addRoom(Room room) {
        rooms.add(room);
    }


    /**
     * Gets the list of rooms in the floor.
     *
     * @return The list of rooms.
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Gets the seed used for generating the floor.
     *
     * @return The seed.
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Sets the seed used for generating the floor.
     *
     * @param seed The seed to set.
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * Gets the level of the floor.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the floor.
     *
     * @param level The level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the list of positions of items that have been picked up
     *
     * @return pickedUpItemsPositions
     */
    public List<Position> getPickedUpItemsPositions(){
        List<Position> pickedUpItemsPositions = new ArrayList<>();
        for(Room room : rooms){
            pickedUpItemsPositions.addAll(room.getPickedUpItemsPositions());
        }
        return pickedUpItemsPositions;
    }

    /**
     * Removes the item from the 2D array and from the items list
     *
     * @param position
     */
    public void removeItemFromRoom(Position position){
        for(Room room : rooms){
            Iterator<Item> itemIterator = room.getItems().iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                if(item.getPosition().getX() == position.getX() && item.getPosition().getY() == position.getY()){
                    setCharacter(position.getY(), position.getX(), ASCIIInterfaceCharacters.ROOM.getCharacter());
                    itemIterator.remove();
                }
            }
        }
    }
}
