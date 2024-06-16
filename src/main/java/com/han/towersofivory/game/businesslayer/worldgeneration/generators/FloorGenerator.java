package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.entities.items.Item;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * FloorGenerator is responsible for generating a floor with rooms and corridors.
 * It uses a random number generator to create rooms of varying sizes and places them within the dungeon.
 * It also generates corridors connecting the rooms and places doors at the entrance of each room.
 * Dead ends in the corridors are also removed to create a more complex dungeon layout.
 */
public class FloorGenerator {
    private final List<Room> roomsToPlace = new ArrayList<>();
    private final int minRoomSize;
    private final int maxRoomSize;
    private int dungeonWidth;
    private int dungeonHeight;
    private Floor floor;
    private CorridorGenerator corridorGenerator;
    private Random random;
    private final ItemGenerator itemGenerator;

    /**
     * Constructor for FloorGenerator.
     *
     * @param dungeonWidth  The width of the dungeon.
     * @param dungeonHeight The height of the dungeon.
     * @param minRoomSize   The minimum size of a room.
     * @param maxRoomSize   The maximum size of a room.
     */
    public FloorGenerator(int dungeonWidth, int dungeonHeight, int minRoomSize, int maxRoomSize) {
        this.minRoomSize = minRoomSize;
        this.maxRoomSize = maxRoomSize;
        this.random = new Random();
        this.dungeonWidth = dungeonWidth;
        this.dungeonHeight = dungeonHeight;
        this.itemGenerator = new ItemGenerator(random);
        corridorGenerator = new CorridorGenerator(floor, random);
    }

    /**
     * Generates the dungeon with rooms, corridors, and doors.
     *
     * @param seed - The seed used for generating the world.
     * @param level - The level of the floor.
     * @return The generated floor.
     */
    public Floor generateFloor(long seed, int level) {
        random.setSeed(seed);
        floor = new Floor(dungeonWidth, dungeonHeight);
        floor.setSeed(seed);
        floor.setLevel(level);
        corridorGenerator.setFloor(floor);
        generateRooms((floor.getWidth() * floor.getHeight()) / (maxRoomSize * minRoomSize) + 1000);
        placeRooms();
        generateCorridors();
        placeDoors();
        removeDeadEnds();
        corridorGenerator.removeInaccessibleTiles();
        removeInaccessibleRooms();
        placeItems();
        placeAllStairs();
        return this.floor;
    }

    /**
     * Generate the rooms of the floor.
     *
     * @param numberOfRooms - The number of rooms that need to be generated.
     */
    private void generateRooms(int numberOfRooms) {
        roomsToPlace.clear();
        for (int i = 0; i < numberOfRooms; i++) {
            int width = random.nextInt(maxRoomSize - minRoomSize) + minRoomSize;
            int height = random.nextInt(maxRoomSize - minRoomSize) + minRoomSize;

            Room room = new Room(width, height);
            room.generateRoom();

            roomsToPlace.add(room);
        }
    }

    /**
     * This method places the rooms on the floor.
     */
    private void placeRooms() {
        for (Room room : roomsToPlace) {
            tryPlacingRoom(room);
        }
    }

    /**
     * This method attempts to place a room on the floor.
     *
     * @param room - The room that needs to be placed.
     */
    private void tryPlacingRoom(Room room) {
        final int MAX_PLACEMENT_TRIES = 50;
        int currentTries = 0;
        while (currentTries < MAX_PLACEMENT_TRIES) {
            int randomX = random.nextInt(floor.getWidth());
            int randomY = random.nextInt(floor.getHeight());

            Tile tile = new Tile(randomX, randomY);

            if (canPlaceRoom(room, tile)) {
                room.setY(randomY);
                room.setX(randomX);
                this.floor.addRoom(room);

                placeRoom(room, tile);
                return;
            }
            currentTries++;
        }
    }


    /**
     * This method places the items on the floor.
     */
    private void placeItems() {
        for (Room room : floor.getRooms()) {
            room.clearItems();
            int amount = calculateAmountOfItems(room, 3);
            List<Item> items = itemGenerator.generateItems(amount);

            items.forEach(item -> {
                int randomX;
                int randomY;

                do {
                    randomX = random.nextInt(room.getWidth() - 2) + 1;
                    randomY = random.nextInt(room.getHeight() - 2) + 1;
                } while (floor.getAsciiCharacterOfTile(new Tile(room.getX() + randomX, room.getY() + randomY)) == ASCIIInterfaceCharacters.DOOR.getCharacter(0) ||
                        floor.getAsciiCharacterOfTile(new Tile(room.getX() + randomX, room.getY() + randomY)) == ASCIIInterfaceCharacters.DOOR.getCharacter(1));

                floor.setCharacter(room.getY() + randomY, room.getX() + randomX, item.getCharacter());
                item.setPosition(new Position(room.getX() + randomX, room.getY() + randomY, floor.getLevel()));
                room.addItem(item);
            });
        }
    }

    /**
     * This method calculates the amount of items that can be placed in a room.
     *
     * @param room - The room where the items need to be placed.
     * @param rate - The rate of items that can be placed in a room.
     * @return The amount of items that can be placed in a room.
     */
    private int calculateAmountOfItems(Room room, int rate) {
        int roomSize = room.getWidth() * room.getHeight();
        return roomSize / 100 * rate;
    }

    /**
     * This method checks if a room can be placed on the floor.
     *
     * @param room - The room that needs to be placed.
     * @param tile - The tile where the room should be placed.
     * @return True if the room can be placed, false otherwise.
     */
    private boolean canPlaceRoom(Room room, Tile tile) {
        if (tile.getX() < 0 || tile.getY() < 0 || tile.getX() + room.getWidth() >= floor.getWidth() || tile.getY() + room.getHeight() >= floor.getHeight()) {
            return false;
        }

        for (int yOffset = 0; yOffset < room.getHeight(); yOffset++) {
            for (int xOffset = 0; xOffset < room.getWidth(); xOffset++) {
                if (floor.getAsciiCharacterOfTile(new Tile(tile.getX() + xOffset, tile.getY() + yOffset)) != ASCIIInterfaceCharacters.SPACE.getCharacter()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This method places the room on the floor.
     *
     * @param room - The room that needs to be placed.
     * @param tile - The tile where the room should be placed.
     */
    private void placeRoom(Room room, Tile tile) {
        for (int yOffset = 0; yOffset < room.getHeight(); yOffset++) {
            for (int xOffset = 0; xOffset < room.getWidth(); xOffset++) {
                floor.setCharacter(tile.getY() + yOffset, tile.getX() + xOffset, room.getCharacter(yOffset, xOffset));
            }
        }
    }

    /**
     * This method generates the corridors on the floor.
     */
    private void generateCorridors() {
        corridorGenerator.createCorridors();
    }

    /**
     * This method places the doors.
     */
    private void placeDoors() {
        List<List<Tile>> potentialDoors = corridorGenerator.findCorridorsAdjacentToRoom(floor.getRooms());
        int index = 0;
        for (List<Tile> potentialDoorsPerRoom : potentialDoors) {
            placeDoorsForRoom(potentialDoorsPerRoom, index);
            index++;
        }
    }

    /**
     * This method places the doors for a room.
     *
     * @param potentialDoorsPerRoom - The potential doors that can be placed for a room.
     * @param roomIndex - The index of the room in the list of rooms.
     */
    private void placeDoorsForRoom(List<Tile> potentialDoorsPerRoom, int roomIndex) {
        if (potentialDoorsPerRoom.isEmpty()) {
            return;
        }

        Collections.shuffle(potentialDoorsPerRoom, random);
        int doorsPlaced = 0;
        for (Tile potentialDoor : potentialDoorsPerRoom) {
            if (doorsPlaced >= 2) {
                break;
            }

            if (isValidDoorLocation(potentialDoor)) {
                placeDoor(potentialDoor);
                floor.getRooms().get(roomIndex).addDoor(potentialDoor.getX(), potentialDoor.getY());
                doorsPlaced++;
            }
        }
    }

    /**
     * This method places a door in a room.
     *
     * @param potentialDoor - The potential door that can be placed.
     */
    private void placeDoor(Tile potentialDoor) {
        if (floor.getAsciiCharacterOfTile(potentialDoor) == ASCIIInterfaceCharacters.ROOM_BORDER.getCharacter(4)) {
            floor.setCharacter(potentialDoor.getY(), potentialDoor.getX(), ASCIIInterfaceCharacters.DOOR.getCharacter(0));
        } else {
            floor.setCharacter(potentialDoor.getY(), potentialDoor.getX(), ASCIIInterfaceCharacters.DOOR.getCharacter(1));
        }
    }

    /**
     * This method checks if the door is placed in a valid location.
     *
     * @param potentialDoor - The potential door that can be placed.
     * @return True if the door location is valid, false otherwise.
     */
    private boolean isValidDoorLocation(Tile potentialDoor) {
        return !corridorGenerator.isCharacter(potentialDoor, ASCIIInterfaceCharacters.ROOM_CORNER);
    }

    /**
     * This method removes the dead ends of the corridors on the floor.
     */
    public void removeDeadEnds() {
        boolean deadEndsToRemove = true;
        while (deadEndsToRemove) {
            deadEndsToRemove = false;
            for (int row = 0; row < floor.getHeight(); row++) {
                for (int col = 0; col < floor.getWidth(); col++) {
                    Tile tile = new Tile(col, row);
                    if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.CORRIDOR) && isDeadEnd(tile)) {
                        floor.setCharacter(row, col, ASCIIInterfaceCharacters.SPACE.getCharacter());
                        deadEndsToRemove = true;
                    }
                }
            }
        }
    }

    /**
     * This method removes inaccessible rooms.
     */
    private void removeInaccessibleRooms() {
        for (Room room : floor.getInaccessibleRooms()) {
            removeRoom(room);
        }
    }

    /**
     * This method removes a room on the floor.
     *
     * @param room - The room that must be removed.
     */
    private void removeRoom(Room room) {
        for (int y = 0; y < room.getHeight(); y++) {
            for (int x = 0; x < room.getWidth(); x++) {
                floor.setCharacter(room.getY() + y, room.getX() + x, ASCIIInterfaceCharacters.SPACE.getCharacter());
            }
        }
    }

    /**
     * This method checks if the tile is a dead end of a corridor path.
     *
     * @param tile - The tile that must be checked.
     * @return True if the tile is a dead end, false otherwise.
     */
    private boolean isDeadEnd(Tile tile) {
        return corridorGenerator.checkCorridorHasOneOrZeroAdjacentCorridors(tile);
    }

    /**
     * This method places all stairs on a floor.
     */
    private void placeAllStairs() {
        List<Room> placedRooms = floor.getRooms();
        Collections.shuffle(placedRooms, random);

        Room room1 = placedRooms.get(0);
        placeStair(room1, ASCIIInterfaceCharacters.STAIRS.getCharacter(0));

        if (placedRooms.size() > 1) {
            if (floor.getLevel() != 0) {
                Room room2 = placedRooms.get(1);
                placeStair(room2, ASCIIInterfaceCharacters.STAIRS.getCharacter(1));
            }
        } else {
            if (floor.getLevel() != 0) {
                placeStair(room1, ASCIIInterfaceCharacters.STAIRS.getCharacter(1));
            }
        }
    }

    /**
     * This method places the stair on a floor.
     *
     * @param room - The room where the stair must be placed.
     * @param stairsCharacter - The character of the stair that must be placed.
     */
    private void placeStair(Room room, char stairsCharacter) {
        int stairsX;
        int stairsY;
        do {
            stairsX = room.getX() + 2 + random.nextInt(room.getWidth() - 2);
            stairsY = room.getY() + 2 + random.nextInt(room.getHeight() - 3);
        } while (!checkStairCanBePlaced(stairsX, stairsY));

        floor.setCharacter(stairsY, stairsX, stairsCharacter);
    }

    /**
     * This method checks if a stair can be placed on the floor.
     *
     * @param stairsX - The x coordinate of the stair.
     * @param stairsY - The y coordinate of the stair.
     * @return True if the stair can be placed, false otherwise.
     */
    private boolean checkStairCanBePlaced(int stairsX, int stairsY) {
        Tile[] tilesToCheck = {
                new Tile(stairsX - 1, stairsY),
                new Tile(stairsX + 1, stairsY),
                new Tile(stairsX, stairsY - 1),
                new Tile(stairsX, stairsY + 1)
        };

        for (Tile tile : tilesToCheck) {
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.DOOR)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.ROOM_BORDER)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.STAIRS)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.CHEST)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.GOLD_PLATING)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.ABSENCE)) {
                return false;
            }
            if (corridorGenerator.isCharacter(tile, ASCIIInterfaceCharacters.C4_MODEL)) {
                return false;
            }

        }
        return true;
    }
}
