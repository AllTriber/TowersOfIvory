package com.han.towersofivory.game.businesslayer.packets;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.packets.DisconnectPacket;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PacketsTest {

    @Test
    void packetsTest() {
        //Arrange/Act
        String[] args = {""};
        ActionPacket actionPacket = new ActionPacket(UUID.randomUUID(), "move", args);
        ConnectionPacket connectionPacket = new ConnectionPacket(UUID.randomUUID(), "1.1.1.1");
        DisconnectPacket disconnectPacket = new DisconnectPacket(UUID.randomUUID(), "1.1.1.1");
        GameStatePacket gameStatePacket = new GameStatePacket(Mockito.mock(GameState.class));
        MovementPacket movementPacket = new MovementPacket(Mockito.mock(Player.class));

        //Assert
        assertNotNull(actionPacket);
        assertNotNull(connectionPacket);
        assertNotNull(disconnectPacket);
        assertNotNull(gameStatePacket);
        assertNotNull(movementPacket);
    }
}
