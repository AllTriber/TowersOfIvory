package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.entities.items.Item;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.*;
import org.apache.logging.log4j.LogManager;

import java.util.Iterator;

/**
 * UC2.2 - Val aan.
 *
 * This class performes the interactions in the world and changes the gamestate.
 */
public class InteractionActionExecutor extends ActionBase {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(InteractionActionExecutor.class);
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final String INTERACTUP = "interactup";
    private static final String INTERACTDOWN = "interactdown";
    private static final String INTERACTRIGHT = "interactright";
    private static final String INTERACTLEFT = "interactleft";
    private final FloorGenerator floorGenerator;


    /**
     * Constructor for InteractionActionExecutor
     *
     * @param floorGenerator the generator of the floor
     */
    public InteractionActionExecutor(FloorGenerator floorGenerator) {
        super(INTERACTUP, null, "Interact with something at given direction.");
        addAliasses(INTERACTDOWN, INTERACTRIGHT, INTERACTLEFT);
        this.floorGenerator = floorGenerator;
    }

    @Override
    public void performAction(Player player, World world, String cmd, String[] args) {
        Position p = player.getPosition();
        switch (cmd.toLowerCase()) {
            case INTERACTUP -> interact(new Position(p.getX(), p.getY()-1, p.getZ()), player, world);
            case INTERACTDOWN -> interact(new Position(p.getX(), p.getY()+1, p.getZ()), player, world);
            case INTERACTRIGHT -> interact(new Position(p.getX()+1, p.getY(), p.getZ()), player, world);
            case INTERACTLEFT -> interact(new Position(p.getX()-1, p.getY(), p.getZ()), player, world);
            default -> LOGGER.error("Invalid direction");
        }
    }
    
    @Override
    public boolean specificCheckAction(Player player, World world, String cmd, String[] args) {
        Position p = player.getPosition();
        char tileContains;
        switch (cmd.toLowerCase()) {
            case INTERACTUP -> tileContains = checkPosition(new Position(p.getX(), p.getY() - 1, p.getZ()), world);
            case INTERACTDOWN -> tileContains = checkPosition(new Position(p.getX(), p.getY() + 1, p.getZ()), world);
            case INTERACTRIGHT -> tileContains = checkPosition(new Position(p.getX() + 1, p.getY(), p.getZ()), world);
            case INTERACTLEFT -> tileContains = checkPosition(new Position(p.getX() - 1, p.getY(), p.getZ()), world);
            default -> {
                return false;
            }
        }
        if (tileContains != ' ') {
            if (tileContains == 'v') {
                int z = player.getPosition().getZ();
                Floor floor = world.getFloors().getFirst();
                if (floor.getLevel() == z) {
                    return false;
                }
            }
            LOGGER.info("Succes! Tile contains interactable!");
            return true;
        }
        else {
            LOGGER.info("Too bad! There is nothing to interact with here...");
            return false;
        }
    }

    /**
     * This method handles the interaction of the player.
     *
     * @param interactionPosition - The position of the interaction.
     * @param player - The player performing the action.
     * @param world - The current gamestate to be changed.
     */
    public void interact(Position interactionPosition, Player player, World world) {
        LOGGER.info("Interact with something at location: x = {}, y = {}, z = {}", interactionPosition.getX(),
                interactionPosition.getY(), interactionPosition.getZ());

        if (checkPosition(interactionPosition, world) == ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter()) {
            handleAttackInteraction(interactionPosition, player, world);
        }
        else if (isItemCharacter(interactionPosition, world, ASCIIInterfaceCharacters.ITEM.getCharacters())) {
            handleItemInteraction(interactionPosition, player, world);
        }
        else if (checkPosition(interactionPosition, world) == ASCIIInterfaceCharacters.STAIRS.getCharacter(0) || checkPosition(interactionPosition, world) == ASCIIInterfaceCharacters.STAIRS.getCharacter(1)) {
            handleStairsInteraction(interactionPosition, player, world);
        }
    }

    private boolean isItemCharacter(Position interactionPosition, World world, char[] itemCharacters) {
        for (char itemCharacter : itemCharacters) {
            if (checkPosition(interactionPosition, world) == itemCharacter) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checkes the position of the interaction.
     *
     * @param interactionPosition - The position of the interaction.
     * @param world - The current gamestate to be changed.
     */
    private char checkPosition(Position interactionPosition, World world) {
        Floor currentFloor = world.getFloor(interactionPosition.getZ());
        Floor floor = world.getFloor(world.getMyPlayer().getPosition().getZ());
        if (currentFloor == null) {
            int levelDif = interactionPosition.getZ() - floor.getLevel();
            long seed = floor.getSeed();
            currentFloor = floorGenerator.generateFloor(seed + levelDif, interactionPosition.getZ());
            world.addFloor(currentFloor);
        }

        char interactionTile = currentFloor.getAsciiCharacterOfTile(new Tile(interactionPosition.getX(), interactionPosition.getY()));

        for(Player player : world.getPlayers()){
            if(player.getPosition().getX() == interactionPosition.getX() && player.getPosition().getY() == interactionPosition.getY()){
                return ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter();
            }
        }

        for(char c : ASCIIInterfaceCharacters.getInteractableCharacters()){
            if(interactionTile == c) {
                return interactionTile;
            }
        }
        return ' ';
    }

    /**
     * This method handles the attack interaction.
     *
     * @param interactionPosition - The position of the interaction.
     * @param player - The player performing the action.
     * @param world - The current gamestate to be changed.
     */
    public void handleAttackInteraction(Position interactionPosition, Player player, World world) {
        for (Player target : world.getPlayers()) {
            if (target.getPosition().getX() == interactionPosition.getX() && target.getPosition().getY() == interactionPosition.getY()) {
                int damage = player.getAttack() - target.getDefense();
                if (damage < 1) {
                    LOGGER.info("Your attacks are too weak to hurt the target!");
                }
                else {
                    LOGGER.info("You dealt {} damage to the target!", damage);
                    target.setHp(target.getHp() - damage);
                }
            }
        }
    }

    /**
     * This method handles the item interaction.
     *
     * @param interactionPosition - The position of the interaction.
     * @param player - The player performing the action.
     * @param world - The current gamestate to be changed.
     */
    public void handleItemInteraction(Position interactionPosition, Player player, World world) {
        Floor currentfloor = world.getFloor(interactionPosition.getZ());
        for (Room room : currentfloor.getRooms()) {
            Iterator<Item> itemIterator = room.getItems().iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                if (item.getPosition().getX() == interactionPosition.getX() && item.getPosition().getY() == interactionPosition.getY()) {
                    item.addItem(player);
                    room.addPickedUpItemPosition(item.getPosition());
                    itemIterator.remove();
                    currentfloor.setCharacter(interactionPosition.getY(), interactionPosition.getX(), ASCIIInterfaceCharacters.ROOM.getCharacter());
                }
            }

        }
    }

    /**
     * This method handles the stairs interaction.
     *
     * @param interactionPosition - The position of the interaction.
     * @param player - The player performing the action.
     * @param world - The current gamestate to be changed.
     */
    public void handleStairsInteraction(Position interactionPosition, Player player, World world) {
        // Determine next floor
        Floor floor = world.getFloor(player.getPosition().getZ());
        int stairsDirection = floor.getStairsDirection(new Tile(interactionPosition.getX(), interactionPosition.getY()));
        Floor nextFloor = world.getFloor(player.getPosition().getZ() + stairsDirection);
        int nextLevel = player.getPosition().getZ() + stairsDirection;

        player.getPosition().setZ(player.getPosition().getZ() + stairsDirection);


        // Generate new floor if the next floor doesn't exist
        if (nextFloor == null) {
            long seed = floor.getSeed();
            Floor newFloor = floorGenerator.generateFloor(seed + stairsDirection, nextLevel);
            world.addFloor(newFloor);
            nextFloor = newFloor;
        }

        // Move the player adjacent to the stairs on the next floor
        int otherStairsDirection = stairsDirection == UP ? DOWN : UP;
        Tile destination = nextFloor.getStairsTile(otherStairsDirection);
        player.getPosition().setX(destination.getX());
        player.getPosition().setY(destination.getY() + 1);
    }
}
