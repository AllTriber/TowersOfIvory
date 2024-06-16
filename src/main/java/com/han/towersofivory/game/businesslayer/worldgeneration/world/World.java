package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * World represents the environment of the game.
 */
public class World implements Serializable {

    private List<Floor> floors;
    private Player myPlayer;
    private AgentConfigurationText myAgentConfiguration;
    private List<Player> otherPlayers;
    private List<AgentConfigurationText> agents;
    private Chat chat;
    private boolean gameStopped = false;
    private static final int MAX_FLOORS = 10;

    /**
     * Constructor for World.
     */
    public World() {
        this.floors = new ArrayList<>();
        this.otherPlayers = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.chat = new Chat();
        this.gameStopped = false;
    }

    /**
     * Spawns the player on a walkable tile on a floor.
     *
     * @param player The player to spawn
     * @param z      The floor level to spawn on
     * @return true if the player was successfully spawned, false otherwise
     */
    public boolean spawnPlayerOnWalkableTile(Player player, int z) {
        int width = getFloor(z).getWidth();
        int height = getFloor(z).getHeight();
        UUID uuid = player.getUUID();
        int hash1 = uuid.hashCode();
        int hash2 = (uuid.toString().hashCode() >> 16);

        int x = Math.abs(hash1 % width);
        int y = Math.abs(hash2 % height);

        while (x == 0 && y == 0) {
            x = Math.abs((hash1 + 1) % width);
            y = Math.abs((hash2 + 1) % height);
            hash1++;
            hash2++;
        }

        // Linear probing for finding a walkable tile
        while (!getFloor(z).isWalkable(x, y)) {
            x = (x + 1) % width;
            if (x == 0) {
                y = (y + 1) % height;
            }
        }

        player.setPosition(new Position(x, y, z));
        return true;
    }

    /**
     * Sets the floors of the world.
     *
     * @param floors List of floors to set
     */
    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    /**
     * Adds a floor to the world. Removes the lowest floor and eliminates players if it exceeds the maximum number of floors.
     *
     * @param floor A floor to add
     */
    public void addFloor(Floor floor) {
        if (this.floors.size() == MAX_FLOORS) {
            Floor floorToRemove = floors.removeFirst();
            eliminatePlayersOnFloor(floorToRemove.getLevel());
        }
        this.floors.add(floor);
    }

    /**
     * Eliminates all players on a floor.
     *
     * @param floorIndex The floor index to eliminate players on
     */
    private void eliminatePlayersOnFloor(int floorIndex) {
        for (Player player : this.otherPlayers) {
            if (player.getPosition().getZ() == floorIndex) {
                player.setHp(0);
            }
        }
        if (this.myPlayer.getPosition().getZ() == floorIndex) {
            this.myPlayer.setHp(0);
        }
    }

    /**
     * Returns the floors of the world.
     *
     * @return List of floors
     */
    public List<Floor> getFloors() {
        return floors;
    }

    /**
     * Returns the player.
     *
     * @return Player
     */
    public Player getMyPlayer() {
        return myPlayer;
    }

    /**
     * Returns the UUID of the player.
     *
     * @return UUID of the player
     */
    public UUID getMyPlayerUUID() {
        return getMyPlayer().getUUID();
    }

    /**
     * Sets the player.
     *
     * @param myPlayer The player to set
     */
    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    /**
     * Removes a player from the game.
     *
     * @param player The player to remove
     */
    public void removePlayer(Player player) {
        otherPlayers.remove(player);
    }

    /**
     * Returns a list of other players in the game.
     *
     * @return List of other players
     */
    public List<Player> getOtherPlayers() {
        return otherPlayers;
    }

    /**
     * Returns a list of all players in the game, including the current player.
     *
     * @return List of all players
     */
    public List<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(myPlayer);
        players.addAll(otherPlayers);
        return players;
    }

    /**
     * Adds a player to the list of other players.
     *
     * @param otherPlayer The player to add
     */
    public void addOtherPlayer(Player otherPlayer) {
        otherPlayers.add(otherPlayer);
    }

    /**
     * Returns the total number of players in the game, including the current player.
     *
     * @return The total number of players
     */
    public int getNumberOfPlayers() {
        //All other players included my player
        return getOtherPlayers().size() + 1;
    }

    /**
     * Returns a floor object based on the floor level.
     *
     * @param floorLevel The floor level to retrieve
     * @return The floor object, or null if the floor level is invalid
     */
    public Floor getFloor(int floorLevel) {
        if (floorLevel < 0) {
            return null;
        }

        for (Floor floor : floors) {
            if (floor.getLevel() == floorLevel) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Returns a list of agent configurations.
     *
     * @return The list of agent configurations
     */
    public List<AgentConfigurationText> getAgents() {
        return agents;
    }

    /**
     * Returns the agent configuration for the current player.
     *
     * @return The agent configuration for the current player
     */
    public AgentConfigurationText getMyAgentConfiguration() {
        return myAgentConfiguration;
    }

    /**
     * Sets the agent configuration for the current player.
     *
     * @param myAgentConfiguration The agent configuration to set
     */
    public void setMyAgentConfiguration(AgentConfigurationText myAgentConfiguration) {
        this.myAgentConfiguration = myAgentConfiguration;
    }

    /**
     * Adds a player to the list of other players.
     *
     * @param player The player to add
     */
    public void setOtherPlayer(Player player) {
        otherPlayers.add(player);
    }

    /**
     * Returns whether the game has been stopped.
     *
     * @return true if the game has been stopped, false otherwise
     */
    public boolean gameStopped() {
        return gameStopped;
    }

    /**
     * Stopgame sets gameStopped to true so every player knows the game has been stopped and can start is own procedure to close the game
     */
    public void stopGame() {
        gameStopped = true;
    }

    /**
     * Returns the floor level of the top floor.
     *
     * @return highest floor level
     */
    public int getHighestFloorLevel() {
        int highestFloorLevel = 0;
        for (Floor floor : floors) {
            if (floor.getLevel() > highestFloorLevel) {
                highestFloorLevel = floor.getLevel();
            }
        }
        return highestFloorLevel;
    }

    /**
     * Returns the maximum number of floors allowed in the game.
     *
     * @return The maximum number of floors
     */
    public int getMaxFloors() {
        return MAX_FLOORS;
    }

    /**
     * Returns the chat object associated with the game.
     *
     * @return The chat object
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Returns a list of UUIDs of players that are in proximity to the given player.
     *
     * @param player The player to check proximity for
     * @return A list of UUIDs of players in proximity, including the given player
     */
    public List<UUID> getPlayersInProximity(Player player) {
        List<UUID> playersInProximity = new ArrayList<>();
        playersInProximity.add(player.getUUID());
        for (Player otherPlayer : otherPlayers) {
            if (player.getPosition().distanceTo(otherPlayer.getPosition()) <= 15) {
                playersInProximity.add(otherPlayer.getUUID());
            }
        }
        return playersInProximity;
    }
}
