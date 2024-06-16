package com.han.towersofivory.ui.businesslayer.services.renderers;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

public class DefaultRenderer implements IWorldRenderer{
    private final int horizontalRange;
    private final int verticalRange;

    public DefaultRenderer(int horizontalRange, int verticalRange) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
    }

    /**
     * Renders the world.
     * The world is rendered with the player in the center and a range of {@link DefaultRenderer#horizontalRange} by {@link DefaultRenderer#verticalRange} tiles around the player
     * Uses the {@link ASCIIInterfaceCharacters} to render the different elements of the world
     *
     * @return The ASCII representation of the world
     */
    @Override
    public String render(World world) {
        int z = world.getMyPlayer().getPosition().getZ();

        StringBuilder sb = new StringBuilder();
        Floor floor = world.getFloor(z);

        if(floor != null) {

            // Get the player's position
            Position playerPos = world.getMyPlayer().getPosition();

            // Calculate the bounds of the area around the player
            int minX = Math.max(0, playerPos.getX() - horizontalRange);
            int maxX = Math.min(floor.getWidth(), playerPos.getX() + horizontalRange);
            int minY = Math.max(0, playerPos.getY() - verticalRange);
            int maxY = Math.min(floor.getHeight(), playerPos.getY() + verticalRange);

            // Copy the floor characters to a new 2D array
            char[][] renderedFloor = new char[maxY - minY][maxX - minX];
            for (int y = minY; y < maxY; y++) {
                for (int x = minX; x < maxX; x++) {
                    renderedFloor[y - minY][x - minX] = floor.getAsciiCharacterOfTile(new Tile(x, y));
                }
            }

            renderPlayers(world, z, renderedFloor, minX, minY);

            // Convert the rendered floor to a string
            for (int y = 0; y < maxY - minY; y++) {
                for (int x = 0; x < maxX - minX; x++) {
                    sb.append(renderedFloor[y][x]);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Renders the players on the floor.
     * Uses the {@link ASCIIInterfaceCharacters} to render the player characters.
     *
     * @param z             The z coordinate of the floor
     * @param renderedFloor The floor to render the players on
     * @param minX          The minimum x coordinate of the area around the player
     * @param minY          The minimum y coordinate of the area around the player
     */
    private void renderPlayers(World world, int z, char[][] renderedFloor, int minX, int minY) {
        // Render the players on the floor
        for (Player player : world.getOtherPlayers()) {
            if(player.getHp() > 0){
                Position position = player.getPosition();
                if (position.getZ() == z) { // Check if the player is on the current floor
                    int x = position.getX() - minX;
                    int y = position.getY() - minY;
                    if (x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length) {
                        renderedFloor[y][x] = ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter();
                    }
                }
            }

        }
        // Render the current player on the floor
        if(world.getMyPlayer().getHp() > 0) {
            renderCurrentPlayer(world, z, renderedFloor, minX, minY);
        }
    }

    private void renderCurrentPlayer(World world, int z, char[][] renderedFloor, int minX, int minY) {
        Position myPosition = world.getMyPlayer().getPosition();
        if (myPosition.getZ() == z) {
            int x = myPosition.getX() - minX;
            int y = myPosition.getY() - minY;
            if (x >= 0 && x < renderedFloor[0].length && y >= 0 && y < renderedFloor.length) {
                renderedFloor[y][x] = ASCIIInterfaceCharacters.PLAYER.getCharacter();
            }
        }
    }
}
