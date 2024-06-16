package com.han.towersofivory.ui.businesslayer.services.renderers;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

/**
 * Concern C18 of stakeholder C3: ASD-studenten
 * Renderer for the fog of war. Key features: <br>
 * 1) Renders corridors within just a small radius of the player and renders them as question marks just outside of that radius.
 * Beyond there corridors are rendered as empty space. <br>
 * 2) The outlines of rooms are always rendered, but the inside of the rooms are rendered as empty space until the player enters them. <br>
 * 3) Items in rooms that the player is not in will be hidden, except for the {@link ASCIIInterfaceCharacters#STAIRS} <br>
 *
 * Implements the {@link IWorldRenderer} interface
 */
public class FogOfWarRenderer implements IWorldRenderer {
    private final int horizontalRange;
    private final int verticalRange;

    /**
     * Constructor for the FogOfWarRenderer.
     * Sets the horizontal and vertical range around the player to render.
     * The world is rendered with the player in the center with a range of {@link #horizontalRange} and {@link #verticalRange} around the player.
     * The font used has a 1:2 aspect ratio, so to compensate for this we can add twice as many characters horizontally as we do vertically.
     *
     * @param horizontalRange The horizontal range out from the player to render
     * @param verticalRange   The vertical range out from the player to render
     */
    public FogOfWarRenderer(int horizontalRange, int verticalRange) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
    }

    /**
     * Renders the world.
     * Uses the {@link ASCIIInterfaceCharacters} to render the different elements of the world
     *
     * @return The ASCII representation of the world
     */
    @Override
    public String render(World world) {
        int z = world.getMyPlayer().getPosition().getZ();
        StringBuilder sb = new StringBuilder();
        Floor floor = world.getFloors().get(z);

        Position playerPos = world.getMyPlayer().getPosition();

        // Calculate the bounds of the area around the player
        int minX = playerPos.getX() - horizontalRange;
        int minY = playerPos.getY() - verticalRange;

        // Declare the two-dimensional char array to build the rendered floor
        char[][] renderedFloor = new char[2 * verticalRange + 1][2 * horizontalRange + 1];

        // Map the world to the new character array, calculating the corridors and hiding the contents of all rooms
        mapWorldToCharacters(floor, playerPos, minX, minY, renderedFloor);

        // Apply flood fill to reveal the room the player is in
        floodFill(renderedFloor, horizontalRange, verticalRange, ASCIIInterfaceCharacters.SPACE.getCharacter(), ASCIIInterfaceCharacters.ROOM.getCharacter());

        // Reveal items in the room the player is in
        revealItemsInCurrentRoom(floor, minX, minY, renderedFloor);

        // Render all players
        renderPlayers(world, z, renderedFloor, minX, minY);

        // Add line breaks to the string builder
        addLineBreaks(sb, renderedFloor);

        return sb.toString();
    }

    /**
     * Add line breaks to the string builder.
     *
     * @param sb The string builder to add the line breaks to
     * @param renderedFloor The floor to add the line breaks to
     */
    private void addLineBreaks(StringBuilder sb, char[][] renderedFloor) {
        for (char[] row : renderedFloor) {
            for (char tile : row) {
                sb.append(tile);
            }
            sb.append("\n");
        }
    }

    /**
     * Reveal items in the room the player is in.
     *
     * @param floor The floor to render
     * @param minX The minimum x-coordinate to render
     * @param minY The minimum y-coordinate to render
     * @param renderedFloor The floor to render
     */
    private void revealItemsInCurrentRoom(Floor floor, int minX, int minY, char[][] renderedFloor) {
        for (int y = 0; y < renderedFloor.length; y++) {
            for (int x = 0; x < renderedFloor[y].length; x++) {
                int worldX = minX + x;
                int worldY = minY + y;
                char tileChar = floor.getAsciiCharacterOfTile(new Tile(worldX, worldY));
                if (isRoomObject(tileChar) && renderedFloor[y][x] == ASCIIInterfaceCharacters.ROOM.getCharacter()) {
                    renderedFloor[y][x] = tileChar;
                }
            }
        }
    }

    /**
     * Maps the world to the new character array, calculating the corridors and hiding the contents of all rooms.
     *
     * @param floor The floor to render
     * @param playerPos The position of the player
     * @param minX The minimum x-coordinate to render
     * @param minY The minimum y-coordinate to render
     * @param renderedFloor The floor to render
     */
    private void mapWorldToCharacters(Floor floor, Position playerPos, int minX, int minY, char[][] renderedFloor) {
        for (int y = 0; y < renderedFloor.length; y++) {
            for (int x = 0; x < renderedFloor[y].length; x++) {
                int worldX = minX + x;
                int worldY = minY + y;

                if (isInsideOfBounds(worldX, worldY, floor)) {
                    char tileChar = floor.getAsciiCharacterOfTile(new Tile(worldX, worldY));
                    renderedFloor[y][x] = tileChar;

                    if (tileChar == ASCIIInterfaceCharacters.CORRIDOR.getCharacter()) {
                        renderedFloor[y][x] = getCharacterForCorridor(worldX, worldY, playerPos);
                    } else if (isRoomObject(tileChar) || tileChar == ASCIIInterfaceCharacters.ROOM.getCharacter()) {
                        renderedFloor[y][x] = ASCIIInterfaceCharacters.SPACE.getCharacter();
                    }
                } else {
                    renderedFloor[y][x] = ASCIIInterfaceCharacters.SPACE.getCharacter();
                }
            }
        }
    }

    /**
     * Check if the coordinates are inside the bounds of the floor.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @param floor The floor to check
     * @return True if the coordinates are inside the bounds of the floor, false otherwise
     */
    private boolean isInsideOfBounds(int x, int y, Floor floor) {
        return x >= 0 && x < floor.getWidth() && y >= 0 && y < floor.getHeight();
    }

    /**
     * Recursive flood fill algorithm to reveal the room around the player.
     *
     * @param floor           The floor on which to preform the algorithm
     * @param x               The x-coordinate of the current tile
     * @param y               The y-coordinate of the current tile
     * @param targetChar      The characters to replace
     * @param replacementChar The character to replace the target with
     */
    private void floodFill(char[][] floor, int x, int y, char targetChar, char replacementChar) {
        // Base case: if the current tile has already been replaced, return.
        if (floor[y][x] == replacementChar) {
            return;
        }

        // Compare the current tile with the target characters
        if (floor[y][x] == targetChar) {
            // Replace the character
            floor[y][x] = replacementChar;

            // Recursive calls in all 4 directions. Checks for reliability of the algorithm.
            if (x + 1 < floor[0].length)
                floodFill(floor, x + 1, y, targetChar, replacementChar);
            if (x - 1 >= 0)
                floodFill(floor, x - 1, y, targetChar, replacementChar);
            if (y + 1 < floor.length)
                floodFill(floor, x, y + 1, targetChar, replacementChar);
            if (y - 1 >= 0)
                floodFill(floor, x, y - 1, targetChar, replacementChar);
        }
    }

    /**
     * Check if the character is an object within the game world.
     * Uses the {@link ASCIIInterfaceCharacters#OBJECTS} enum to check if the character is an object.
     *
     * @param tileChar The character to check
     * @return True if the character is an object, false otherwise
     */
    private boolean isRoomObject(char tileChar) {
        for (char object : ASCIIInterfaceCharacters.OBJECTS.getCharacters()) {
            if (tileChar == object) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the character to render for the corridor.
     * The character is based on the distance from the player.
     * If the distance is greater than the unknown ring radius, the character is a question mark.
     * If the distance is greater than the unknown ring radius + the semi-reveal width, the character is an empty space.
     * Otherwise, the character is a corridor.
     *
     * @param x         The x-coordinate of the tile
     * @param y         The y-coordinate of the tile
     * @param playerPos The position of the player
     * @return The character to render
     */
    private char getCharacterForCorridor(int x, int y, Position playerPos) {
        double distanceFromPlayer = getDistanceFromPlayer(x, y, playerPos);
        int unknownRingRadius = 10;
        int semiRevealWidth = 3;
        if (distanceFromPlayer > unknownRingRadius + semiRevealWidth) {
            return ASCIIInterfaceCharacters.SPACE.getCharacter();
        } else if (distanceFromPlayer > unknownRingRadius) {
            return ASCIIInterfaceCharacters.UNKNOWN.getCharacter();
        } else {
            return ASCIIInterfaceCharacters.CORRIDOR.getCharacter();
        }
    }

    /**
     * Get the distance from the player to a given position using the Pythagorean theorem.
     *
     * @param x The x-coordinate of searched tile.
     * @param y The y-coordinate of searched tile.
     * @param playerPos The position of the player.
     * @return The distance from the player to the searched tile.
     */
    private double getDistanceFromPlayer(int x, int y, Position playerPos) {
        return Math.sqrt(Math.pow(x - playerPos.getX(), 2) + Math.pow((y - playerPos.getY()) * 2, 2));
    }

    /**
     * Render all players in the world. (Other players and the current player)
     *
     * @param world The world to render
     * @param z The z-coordinate of the floor to render
     * @param renderedFloor The floor to render
     * @param minX The minimum x-coordinate to render
     * @param minY The minimum y-coordinate to render
     */
    private void renderPlayers(World world, int z, char[][] renderedFloor, int minX, int minY) {
        for (Player player : world.getOtherPlayers()) {
            if (player.getHp() > 0) {
                Position position = player.getPosition();
                if (position.getZ() == z) {
                    int x = position.getX() - minX;
                    int y = position.getY() - minY;
                    double distance = getDistanceFromPlayer(x, y, world.getMyPlayer().getPosition());
                    if (distance <= horizontalRange && x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length) {
                        renderedFloor[y][x] = ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter();
                    }
                }
            }
        }
        if (world.getMyPlayer().getHp() > 0) {
            renderCurrentPlayer(world, z, renderedFloor, minX, minY);
        }
    }

    /**
     * Render the current player.
     *
     * @param world The world to render
     * @param z The z-coordinate of the floor to render
     * @param renderedFloor The floor to render
     * @param minX The minimum x-coordinate to render
     * @param minY The minimum y-coordinate to render
     */
    private void renderCurrentPlayer(World world, int z, char[][] renderedFloor, int minX, int minY) {
        Position myPosition = world.getMyPlayer().getPosition();
        if (myPosition.getZ() == z) {
            int x = myPosition.getX() - minX;
            int y = myPosition.getY() - minY;
            double distance = Math.sqrt(Math.pow(myPosition.getX() - world.getMyPlayer().getPosition().getX(), 2) +
                    Math.pow(myPosition.getY() - world.getMyPlayer().getPosition().getY(), 2));
            if (distance <= horizontalRange && isInsideRenderedWindow(x, y, renderedFloor)) {
                renderedFloor[y][x] = ASCIIInterfaceCharacters.PLAYER.getCharacter();
            }
        }
    }

    /**
     * Check if the player is inside the rendered window.
     *
     * @param x The x-coordinate of the player
     * @param y The y-coordinate of the player
     * @param renderedFloor The floor to render
     * @return True if the player is inside the rendered window, false otherwise
     */
    private boolean isInsideRenderedWindow(int x, int y, char[][] renderedFloor) {
        return x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length;
    }
}
