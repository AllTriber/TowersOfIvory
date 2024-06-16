package com.han.towersofivory.game.businesslayer.gamecontroller.listeners.host;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.packets.AgentConfigurationPacket;
import com.han.towersofivory.network.interfacelayer.interfaces.IHost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

class ServerPacketListenersTest {
    @Mock
    private GameController game;

    @Mock
    private IHost network;

    @InjectMocks
    private ServerPacketListeners serverPacketListeners;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPacketHandler_ActionPacket() {
        // Arrange
        UUID playerUUID = UUID.randomUUID();
        String action = "someAction";
        String[] args = {"arg1", "arg2"};
        ActionPacket actionPacket = mock(ActionPacket.class);
        when(actionPacket.getPlayerUUID()).thenReturn(playerUUID);
        when(actionPacket.getAction()).thenReturn(action);
        when(actionPacket.getArgs()).thenReturn(args);

        // Act
        serverPacketListeners.packetHandler(actionPacket);

        // Assert
        verify(game, times(1)).handleActionOnHost(playerUUID, action, args);
    }

    @Test
    void testPacketHandler_AgentConfigurationPacket() {
        // Arrange
        UUID targetUUID = UUID.randomUUID();
        String config = "config";

        // Create a mock AgentConfigurationText object
        AgentConfigurationText agentConfig = mock(AgentConfigurationText.class);
        when(agentConfig.getConfiguration()).thenReturn(config);

        // Create a mock AgentConfigurationPacket object
        AgentConfigurationPacket agentConfigurationPacket = mock(AgentConfigurationPacket.class);
        when(agentConfigurationPacket.getTargetUUID()).thenReturn(targetUUID);
        when(agentConfigurationPacket.getAgentConfig()).thenReturn(agentConfig);

        // Act
        serverPacketListeners.packetHandler(agentConfigurationPacket);

        // Assert
        verify(network, times(1)).sendPacketToClients(agentConfigurationPacket);
    }

}
