package com.han.towersofivory.game.interfacelayer.interfaces;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.GameWorldService;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IGame {

    /*
     * UC2 Spelen Spel
     *
     * Checks the action on the side of the host.
     *
     * @param player - The player performing the action
     * @param command - the input command the player entered into the TUI.
     * @param args - the optional arguments for the command the player entered into the TUI.
     * @return whether the action can be completed without problems
     */
    boolean checkAction(UUID player, String cmd, String[] args);

    /*
     * UC2 Spelen Spel
     *
     * Perfroms an action on the side of the host, and changes the gamestate accordingly.
     * Also sends the action to all peers in the game.
     *
     * @param player - The player performing the action
     * @param command - the input command the player entered into the TUI.
     * @param args - the optional arguments for the command the player entered into the TUI.
     */
    void performAction(UUID player, String cmd, String[] args) throws IOException;

    void configureGame(GameConfiguration configuration);

    void handleJoin(UUID uuid);

    void handleLeave(UUID uuid);

    HostInformation createGame(GameInfo gameInfo) throws ConnectionFailedException;

    void stopGame();

    void joinGame(HostInformation hostInformation, AgentConfigurationText selectedAgentConfig) throws ConnectionFailedException, IOException;

    void sendInput(String input);

    World getWorld();

    List<HostInformation> getHosts();

    void generateWorld(GameConfiguration gameConfiguration);

    GameConfiguration getGameConfiguration();

    void saveAgentConfiguration(AgentConfigurationText agentConfigurationText);
    GameState getGameState();

    void setGameState(GameState gameState);
    
    Long getSeed(String gameSeed);

    GameWorldService getGameWorldService();

    void saveGameState() throws IOException;

    void updatePlayers(GameStatePacket gameStatePacket);

    void handleLowestFloorLevelUpdate(GameStatePacket gameStatePacket);
}
