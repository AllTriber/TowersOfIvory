package com.han.towersofivory.game.businesslayer.entities;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

import java.util.HashMap;
import java.util.UUID;

public class Player extends DynamicEntity {

    private UUID uuid;
    private String otherPlayers = "otherPlayers";

    @SuppressWarnings("java:S1948")
    private PlayerVision playerVision;

    public Player(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid=" + uuid +
                '}';
    }

    public PlayerVision getPlayerVision() {
        return playerVision;
    }

    /**
     * Update the player's vision based on their position in the world
     *
     * @param world   the current world
     * @param command the last movement command
     */
    public void updatePlayerVision(World world, String command) {
        int playerX = this.getPosition().getX();
        int playerY = this.getPosition().getY();
        int playerZ = this.getPosition().getZ();

        boolean isOnDoorTile = checkIfOnDoorTile(world, playerX, playerY, playerZ);
        boolean zLevelChanged = this.playerVision == null || playerZ != this.playerVision.getZ();

        if (shouldUpdateVision(isOnDoorTile, zLevelChanged)) {
            updateVisionBasedOnPosition(world, command, playerX, playerY, playerZ);
        } else {
            this.playerVision.setLastMovement(command);
        }

        updateOtherPlayersInRoom(world);
        updateObjectsInRoom(world);

        this.playerVision.setHp(this.getHp());
        this.playerVision.setAtk(this.getAttack());
        this.playerVision.setDef(this.getDefense());
    }

    /**
     * Check if the player is on a door tile
     *
     * @param world   the current world
     * @param playerX the player's x-coordinate
     * @param playerY the player's y-coordinate
     * @return true if the player is on a door tile, false otherwise
     */
    boolean checkIfOnDoorTile(World world, int playerX, int playerY, int playerZ) {
        for (Room room : world.getFloor(playerZ).getRooms()) {
            for (Tile door : room.getDoors()) {
                if (door.getX() == playerX && door.getY() == playerY) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the player's vision should be updated
     *
     * @param isOnDoorTile  true if the player is on a door tile, false otherwise
     * @param zLevelChanged true if the player has changed z-level, false otherwise
     * @return true if the player's vision should be updated, false otherwise
     */
    public boolean shouldUpdateVision(boolean isOnDoorTile, boolean zLevelChanged) {
        return this.playerVision == null || isOnDoorTile || zLevelChanged;
    }

    /**
     * Update the player's vision based on their position in the world
     *
     * @param world   the current world
     * @param command the last movement command
     * @param playerX the player's x-coordinate
     * @param playerY the player's y-coordinate
     * @param playerZ the player's z-coordinate
     */
    private void updateVisionBasedOnPosition(World world, String command, int playerX, int playerY, int playerZ) {
        for (Room room : world.getFloor(playerZ).getRooms()) {
            if (isPlayerInRoom(room, playerX, playerY)) {
                updateVisionForRoom(room, command, playerZ);
                return;
            }
        }
        updateVisionForCorridor(command, playerZ);
    }

    /**
     * Check if the player is in a room
     *
     * @param room    the room to check
     * @param playerX the player's x-coordinate
     * @param playerY the player's y-coordinate
     * @return true if the player is in the room, false otherwise
     */
    private boolean isPlayerInRoom(Room room, int playerX, int playerY) {
        return room.getX() <= playerX && room.getY() <= playerY &&
                room.getX() + room.getWidth() > playerX && room.getY() + room.getHeight() > playerY;
    }

    /**
     * Update the player's vision for a room
     *
     * @param room    the room the player is in
     * @param command the last movement command
     * @param playerZ the player's z-coordinate
     */
    private void updateVisionForRoom(Room room, String command, int playerZ) {
        if (this.playerVision == null) {
            this.playerVision = new PlayerVision(room, true, command);
        } else {
            this.playerVision.setRoom(room);
            this.playerVision.setInRoom(true);
            this.playerVision.setZ(playerZ);
            this.playerVision.setLastMovement(command);
        }
    }

    /**
     * Update the player's vision for a corridor
     *
     * @param command the last movement command
     * @param playerZ the player's z-coordinate
     */
    private void updateVisionForCorridor(String command, int playerZ) {
        if (this.playerVision == null) {
            this.playerVision = new PlayerVision(null, false, command);
        } else {
            this.playerVision.setRoom(null);
            this.playerVision.setInRoom(false);
            this.playerVision.setZ(playerZ);
            this.playerVision.setLastMovement(command);
        }
    }

    /**
     * Update the number of other players in the room
     *
     * @param world the current world
     */
    public void updateOtherPlayersInRoom(World world) {
        int otherPlayersInRoom = 0;
        HashMap<UUID, Position> otherPlayersPositions = new HashMap<>();
        for (Player otherPlayer : world.getOtherPlayers()) {
            if (otherPlayer.getPosition().getZ() == this.getPosition().getZ()) {
                Room otherPlayerRoom = getRoomForPlayer(otherPlayer, world);
                if (otherPlayerRoom != null && otherPlayerRoom.equals(this.playerVision.getRoom())) {
                    otherPlayersInRoom++;
                    otherPlayersPositions.put(otherPlayer.getUUID(), otherPlayer.getPosition());
                }
            }
        }
        this.playerVision.getObjectsInRoom().put(otherPlayers, otherPlayersInRoom);
        this.playerVision.getOtherPlayersInRoom().clear();
        this.playerVision.getOtherPlayersInRoom().putAll(otherPlayersPositions);
    }

    /**
     * Update the number of objects in the room
     *
     * @param world the current world
     */
    public void updateObjectsInRoom(World world) {
        int doorCount = 0;
        int itemCount = 0;
        int otherPlayersCount = this.playerVision.getObjectsInRoom().get(otherPlayers);

        if (this.playerVision.getInRoom()) {
            Room room = this.playerVision.getRoom();
            doorCount = room.getDoors().size();
            itemCount = room.getItems().size();
        } else {
            // Player is in a corridor
            doorCount = 0;
            itemCount = 0;
        }

        this.playerVision.getObjectsInRoom().put("doors", doorCount);
        this.playerVision.getObjectsInRoom().put(otherPlayers, otherPlayersCount);
        this.playerVision.getObjectsInRoom().put("items", itemCount);
    }

    /**
     * Get the room the player is in
     *
     * @param player the player
     * @param world  the current world
     * @return the room the player is in
     */
    public Room getRoomForPlayer(Player player, World world) {
        int playerX = player.getPosition().getX();
        int playerY = player.getPosition().getY();
        for (Room room : world.getFloor(player.getPosition().getZ()).getRooms()) {
            if (room.getX() <= playerX && room.getY() <= playerY &&
                    room.getX() + room.getWidth() > playerX && room.getY() + room.getHeight() > playerY) {
                return room;
            }
        }
        return null;
    }

    public void setPlayerVision(PlayerVision left) {
        this.playerVision = left;

    }
}
