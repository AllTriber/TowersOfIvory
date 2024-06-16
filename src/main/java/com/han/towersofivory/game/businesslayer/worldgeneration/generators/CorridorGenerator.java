package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;

import java.util.*;

/**
 * CorridorGenerator is responsible for generating corridors in a dungeon.
 * It uses a random number generator to create corridors of varying lengths and directions.
 * It also identifies potential door locations adjacent to rooms.
 * Dead ends in the corridors are also identified and can be removed in the DungeonGenerator class.
 */
public class CorridorGenerator {
    private Random random;
    private Floor floor;

    /**
     * Constructor for CorridorGenerator.
     *
     * @param floor  The dungeon in which corridors are to be generated.
     * @param random The random number generator.
     */
    public CorridorGenerator(Floor floor, Random random) {
        this.floor = floor;
        this.random = random;
    }

    public void removeInaccessibleTiles() {
        Tile randomWalkableTile = getRandomWalkableTile();
        if (randomWalkableTile != null) {
            boolean[][] visited = breadthFirstSearchWalkableTiles(randomWalkableTile);
            List<Tile> inaccessibleTiles = findInaccessibleTiles(visited);
            markInaccessibleTiles(inaccessibleTiles);
        }
    }

    private boolean[][] breadthFirstSearchWalkableTiles(Tile startTile) {
        boolean[][] visited = new boolean[floor.getHeight()][floor.getWidth()];
        Queue<Tile> queue = new LinkedList<>();
        queue.add(startTile);
        visited[startTile.getY()][startTile.getX()] = true;

        while (!queue.isEmpty()) {
            Tile currentTile = queue.poll();
            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newX = currentTile.getX() + direction[0];
                int newY = currentTile.getY() + direction[1];

                if (isValidCoordinate(newX, newY) && !visited[newY][newX] && floor.isWalkable(newX, newY)) {
                    visited[newY][newX] = true;
                    queue.add(new Tile(newX, newY));
                }
            }
        }

        return visited;
    }

    private List<Tile> findInaccessibleTiles(boolean[][] visited) {
        List<Tile> inaccessibleTiles = new ArrayList<>();
        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                if (floor.isWalkable(col, row) && !visited[row][col]) {
                    inaccessibleTiles.add(new Tile(col, row));
                }
            }
        }
        return inaccessibleTiles;
    }

    private void markInaccessibleTiles(List<Tile> inaccessibleTiles) {
        floor.removeInaccessibleRooms(inaccessibleTiles);
        for (Tile tile : inaccessibleTiles) {
            floor.setCharacter(tile.getY(), tile.getX(), ASCIIInterfaceCharacters.SPACE.getCharacter());
        }
    }

    /**
     * Gets a random walkable tile from the floor.
     * A walkable tile is either a corridor, a room, or a door.
     *
     * @return A randomly chosen walkable tile, or null if there are no walkable tiles.
     */
    public Tile getRandomWalkableTile() {
        List<Tile> walkableTiles = new ArrayList<>();
        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                Tile tile = new Tile(col, row);
                if (isWalkable(tile)) {
                    walkableTiles.add(tile);
                }
            }
        }
        if (walkableTiles.isEmpty()) {
            return null;
        }
        return walkableTiles.get(random.nextInt(walkableTiles.size()));
    }

    /**
     * Checks if the given coordinates are within the valid range of the floor's dimensions.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < floor.getWidth() && y >= 0 && y < floor.getHeight();
    }

    /**
     * Checks if a given tile is walkable.
     * A walkable tile is defined as either a corridor, a room, or a door.
     *
     * @param tile The tile to check.
     * @return True if the tile is walkable, false otherwise.
     */
    private boolean isWalkable(Tile tile) {
        return isCharacter(tile, ASCIIInterfaceCharacters.CORRIDOR) || isCharacter(tile, ASCIIInterfaceCharacters.ROOM) || isCharacter(tile, ASCIIInterfaceCharacters.DOOR);
    }

    /**
     * Creates corridors in the dungeon starting from certain points.
     */
    public void createCorridors() {
        List<Tile> corridorStartingPoints = getCorridorStartingPoints();
        while (!corridorStartingPoints.isEmpty()) {
            Tile randomStartingPoint = corridorStartingPoints.get(random.nextInt(getCorridorStartingPoints().size()));
            recursiveCorridorPlacementInRandomDirection(randomStartingPoint, randomStartingPoint, random);
            corridorStartingPoints = getCorridorStartingPoints();
        }
    }

    /**
     * Gets the starting points for corridors in the dungeon.
     *
     * @return A list of tiles that can be used as starting points for corridors.
     */
    private List<Tile> getCorridorStartingPoints() {
        List<Tile> startingPoints = new ArrayList<>();

        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                Tile tile = new Tile(col, row);
                if (isCharacter(tile, ASCIIInterfaceCharacters.SPACE) && isAdjacentToSpace(tile)) {
                    startingPoints.add(tile);
                }
            }
        }

        return startingPoints;
    }

    /**
     * Places corridors in random directions recursively starting from a given tile.
     *
     * @param currentTile  The current tile in the corridor.
     * @param originalTile The original starting tile of the corridor.
     * @param random       The random number generator.
     */
    private void recursiveCorridorPlacementInRandomDirection(Tile currentTile, Tile originalTile, Random random) {
        List<Character> randomDirections = getRandomDirections(random);

        floor.setCharacter(currentTile.getY(), currentTile.getX(), ASCIIInterfaceCharacters.CORRIDOR.getCharacter());

        for (char direction : randomDirections) {
            int nextX = currentTile.getX();
            int nextY = currentTile.getY();

            switch (direction) {
                case 'N':
                    nextY--;
                    break;
                case 'E':
                    nextX++;
                    break;
                case 'S':
                    nextY++;
                    break;
                case 'W':
                    nextX--;
                    break;
                default:
                    continue;
            }

            Tile nextTile = new Tile(nextX, nextY);
            if (isValid(nextTile) && (nextX != originalTile.getX() || nextY != originalTile.getY())) {
                recursiveCorridorPlacementInRandomDirection(nextTile, originalTile, random);
            }
        }
    }

    /**
     * Gets a list of random directions (N, S, E, W) for corridor placement.
     *
     * @param random The random number generator.
     * @return A shuffled list of directions.
     */
    private List<Character> getRandomDirections(Random random) {
        List<Character> directions = new ArrayList<>();
        directions.add('N');
        directions.add('S');
        directions.add('W');
        directions.add('E');
        Collections.shuffle(directions, random);
        return directions;
    }

    /**
     * Finds corridors that are adjacent to rooms.
     *
     * @return A list of lists of tiles, each list representing potential door locations for a room.
     */
    public List<List<Tile>> findCorridorsAdjacentToRoom(List<Room> placedRooms) {
        List<List<Tile>> corridorsAroundRoom = new ArrayList<>();

        for (Room placedRoom : floor.getRooms()) {
            corridorsAroundRoom.add(findCorridorsForRoom(placedRoom));
        }

        return corridorsAroundRoom;
    }

    /**
     * Finds corridors adjacent to a given room.
     *
     * @param placedRoom The room for which adjacent corridors are to be found.
     * @return A list of tiles representing the corridors adjacent to the room.
     */
    private List<Tile> findCorridorsForRoom(Room placedRoom) {
        List<Tile> corridorAdjacentCells = new ArrayList<>();

        for (int row = placedRoom.getY() - 1; row <= placedRoom.getY() + placedRoom.getHeight(); row++) {
            for (int col = placedRoom.getX() - 1; col <= placedRoom.getX() + placedRoom.getWidth(); col++) {
                Tile tile = new Tile(col, row);
                if (isCorridorCandidate(tile, placedRoom)) {
                    int adjustedRow = adjustDimension(row, placedRoom.getY(), placedRoom.getHeight(), true);
                    int adjustedCol = adjustDimension(col, placedRoom.getX(), placedRoom.getWidth(), false);
                    Tile adjacentCell = new Tile(adjustedCol, adjustedRow);
                    corridorAdjacentCells.add(adjacentCell);
                }
            }
        }

        return corridorAdjacentCells;
    }

    /**
     * Checks if a tile is a candidate for being adjacent to a corridor.
     *
     * @param tile       The tile to check.
     * @param placedRoom The room adjacent to the tile.
     * @return True if the tile is a valid corridor candidate, false otherwise.
     */
    private boolean isCorridorCandidate(Tile tile, Room placedRoom) {
        return isWithinDungeon(tile) && isCorridorAdjacentCell(tile, placedRoom) && !isCornerCell(tile);
    }

    /**
     * Adjusts a coordinate to ensure it is within the bounds of the dungeon.
     *
     * @param value     The value to adjust.
     * @param roomStart The start coordinate of the room.
     * @param roomSize  The size of the room.
     * @param isRow     True if adjusting a row coordinate, false if adjusting a column coordinate.
     * @return The adjusted coordinate.
     */
    private int adjustDimension(int value, int roomStart, int roomSize, boolean isRow) {
        if (value >= roomStart && value < roomStart + roomSize) {
            return value;
        } else if (value < roomStart) {
            return Math.min(value + 1, isRow ? floor.getHeight() - 1 : floor.getWidth() - 1);
        } else {
            return Math.max(roomStart + roomSize - 1, 0);
        }
    }

    /**
     * Checks if a tile is a corner cell of a room.
     *
     * @param tile The tile to check.
     * @return True if the tile is a corner cell, false otherwise.
     */
    private boolean isCornerCell(Tile tile) {
        return isCharacter(tile, ASCIIInterfaceCharacters.ROOM_CORNER);
    }

    /**
     * Checks if a tile is a corridor adjacent to a room.
     *
     * @param tile The tile to check.
     * @param room The room adjacent to the tile.
     * @return True if the tile is a corridor adjacent to the room, false otherwise.
     */
    private boolean isCorridorAdjacentCell(Tile tile, Room room) {
        return tile.getY() >= 0 && tile.getY() < floor.getHeight() && tile.getX() >= 0 && tile.getX() < floor.getWidth() &&
                !(tile.getY() >= room.getY() && tile.getY() < room.getY() + room.getHeight() && tile.getX() >= room.getX() && tile.getX() < room.getX() + room.getWidth()) &&
                floor.getAsciiCharacterOfTile(tile) == ASCIIInterfaceCharacters.CORRIDOR.getCharacter();
    }

    /**
     * Checks if a tile is adjacent to an empty space.
     *
     * @param tile The tile to check.
     * @return True if the tile is adjacent to an empty space, false otherwise.
     */
    private boolean isAdjacentToSpace(Tile tile) {
        boolean adjacentTop = tile.getY() > 0 && isCharacter(new Tile(tile.getX(), tile.getY() - 1), ASCIIInterfaceCharacters.SPACE);
        boolean adjacentBottom = tile.getY() < floor.getHeight() - 1 && isCharacter(new Tile(tile.getX(), tile.getY() + 1), ASCIIInterfaceCharacters.SPACE);
        boolean adjacentLeft = tile.getX() > 0 && isCharacter(new Tile(tile.getX() - 1, tile.getY()), ASCIIInterfaceCharacters.SPACE);
        boolean adjacentRight = tile.getX() < floor.getWidth() - 1 && isCharacter(new Tile(tile.getX() + 1, tile.getY()), ASCIIInterfaceCharacters.SPACE);

        return adjacentTop && adjacentBottom && adjacentLeft && adjacentRight;
    }

    /**
     * Checks if a tile is valid for placing a corridor.
     *
     * @param tile The tile to check.
     * @return True if the tile is valid for placing a corridor, false otherwise.
     */
    private boolean isValid(Tile tile) {
        return isWithinDungeon(tile) && !isCharacter(tile, ASCIIInterfaceCharacters.ROOM_BORDER) && !isCharacter(tile, ASCIIInterfaceCharacters.CORRIDOR) && checkCorridorHasOneOrZeroAdjacentCorridors(tile);
    }

    /**
     * Checks if a tile is within the dungeon boundaries.
     *
     * @param tile The tile to check.
     * @return True if the tile is within the dungeon, false otherwise.
     */
    private boolean isWithinDungeon(Tile tile) {
        return tile.getX() >= 0 && tile.getX() < floor.getWidth() && tile.getY() >= 0 && tile.getY() < floor.getHeight();
    }

    /**
     * Checks if a certain character is present at a given coordinate in the dungeon.
     *
     * @param tile      The coordinate to check.
     * @param character The character to check for.
     * @return True if the character is present at the coordinate, false otherwise.
     */
    public boolean isCharacter(Tile tile, ASCIIInterfaceCharacters character) {
        char dungeonCharacter = floor.getAsciiCharacterOfTile(tile);
        char[] characters = character.getCharacters();
        for (char characterToCheck : characters) {
            if (characterToCheck == dungeonCharacter) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a corridor has one or zero adjacent corridors.
     *
     * @param tile The coordinate of the corridor to check.
     * @return True if the corridor has one or zero adjacent corridors, false otherwise.
     */
    public boolean checkCorridorHasOneOrZeroAdjacentCorridors(Tile tile) {
        int adjacentCorridors = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int newX = tile.getX() + direction[0];
            int newY = tile.getY() + direction[1];

            if (newX >= 0 && newX < floor.getWidth() && newY >= 0 && newY < floor.getHeight()) {
                char character = floor.getAsciiCharacterOfTile(new Tile(newX, newY));
                char[] doorCharacters = ASCIIInterfaceCharacters.DOOR.getCharacters();
                if (character == ASCIIInterfaceCharacters.CORRIDOR.getCharacter()
                        || new String(doorCharacters).contains(String.valueOf(character))) {
                    adjacentCorridors++;
                }
            }
        }

        return adjacentCorridors <= 1;
    }



    public Floor getFloor() {
        return floor;
    }


    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
