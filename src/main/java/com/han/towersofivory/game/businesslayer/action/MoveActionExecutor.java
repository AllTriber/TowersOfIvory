package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

/*
 * UC2 Spelen Spel -> US2.2 Bewegen spelkarakter
 *
 * The purpose of this class is to move characters in the game world.
 */
public class MoveActionExecutor extends ActionBase {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(MoveActionExecutor.class);

    List<Character> walkableCharacters = new ArrayList<>();

    public MoveActionExecutor() {
        super("up", null, "Moves a player to the direction.");
        addAliasses("down", "right", "left");
        addAliasses("north", "south", "west", "east");

        char[] walkableChars = ASCIIInterfaceCharacters.getWalkableCharacters();
        for (char c : walkableChars) {
            walkableCharacters.add(c);
        }

    }

    private boolean updatePositionBasedOnMoveActionCmd(String cmd, Position newPosition) {
        switch (cmd.toLowerCase()) {
            case "up", "north" -> newPosition.adjustY(-1);
            case "down", "south" -> newPosition.adjustY(+1);
            case "right", "east" -> newPosition.adjustX(+1);
            case "left", "west" -> newPosition.adjustX(-1);
            default -> {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the player to the new position.
     *
     * @param actingPlayer the player that wants to move
     * @param world        the current gamestate
     * @param cmd          the command that the player wants to execute (up, down, left, right)
     * @param args         the optional arguments for the command the player entered into the TUI.
     */
    @Override
    public void performAction(Player actingPlayer, World world, String cmd, String[] args) {
        if (args.length != 0)
            return;

        Position newPosition = actingPlayer.getPosition();
        updatePositionBasedOnMoveActionCmd(cmd, newPosition);

        world.getOtherPlayers().forEach(player -> {
            if (player.getUUID().equals(actingPlayer.getUUID())) {
                player.setPosition(newPosition);
            }
        });

    }

    /**
     * Checks if the action is possible. This is done by checking if the new position is a walkable tile.
     *
     * @param player the player that wants to move
     * @param world  the current gamestate
     * @param cmd    the command that the player wants to execute (up, down, left, right)
     * @param args   the optional arguments for the command the player entered into the TUI.
     * @return whether the action can be completed without problems
     */
    @Override
    public boolean specificCheckAction(Player player, World world, String cmd, String[] args) {
        LOGGER.info("Checking action");
        LOGGER.info(getNewPosition(player, cmd));

        Position newPosition = getNewPosition(player, cmd);

        Floor floorOfPlayer = world.getFloor(player.getPosition().getZ());

        if (newPosition == null) {
            return false;
        }

        Tile newTileOfPlayer = new Tile(newPosition.getX(), newPosition.getY());

        char characterOfTile = floorOfPlayer.getAsciiCharacterOfTile(newTileOfPlayer);

        return walkableCharacters.contains(characterOfTile);
    }

    /**
     * determines the new position of the given player, based on the command from the player
     *
     * @param actingPlayer the player that wants to move
     * @param cmd          the command that the player wants to execute (up, down, left, right)
     */
    private Position getNewPosition(Player actingPlayer, String cmd) {
        Position currentPosition = actingPlayer.getPosition();
        Position newPosition = new Position(currentPosition.getX(), currentPosition.getY(), currentPosition.getZ());
        return updatePositionBasedOnMoveActionCmd(cmd, newPosition) ? null : newPosition;
    }
}
