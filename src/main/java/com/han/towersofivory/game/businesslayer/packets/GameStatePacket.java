package com.han.towersofivory.game.businesslayer.packets;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

/*
 * Packet that is sent through TCP connection to share the current gamestate with all players.
 */
public class GameStatePacket extends BasePacket {
    private GameState gameState;

    public GameStatePacket(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
