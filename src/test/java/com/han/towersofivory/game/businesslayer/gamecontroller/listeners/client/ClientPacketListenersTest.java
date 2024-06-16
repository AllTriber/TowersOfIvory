package com.han.towersofivory.game.businesslayer.gamecontroller.listeners.client;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.packets.AgentConfigurationPacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.GameWorldService;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.packets.DisconnectPacket;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientPacketListenersTest {

    @Mock
    private IGame game;

    @Mock
    private GameController gameController;

    @Mock
    private IClient network;

    @Mock
    private GameWorldService gameWorldService;

    @Mock
    private FloorGenerator floorGenerator;

    @InjectMocks
    private ClientPacketListeners clientPacketListeners;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPacketHandler_ActionPacket() throws IOException {
        // Arrange
        UUID playerUUID = UUID.randomUUID();
        String action = "someAction";
        String[] args = {"arg1", "arg2"};
        ActionPacket actionPacket = mock(ActionPacket.class);
        when(actionPacket.getPlayerUUID()).thenReturn(playerUUID);
        when(actionPacket.getAction()).thenReturn(action);
        when(actionPacket.getArgs()).thenReturn(args);

        // Act
        clientPacketListeners.packetHandler(actionPacket);

        // Assert
        verify(gameController, times(1)).handleActionOnPeer(playerUUID, action, args);
    }

    @Test
    void testOnConnection_OwnPlayerAlreadySet() {
        // Arrange
        UUID connectionUUID = UUID.randomUUID();
        ConnectionPacket connectionPacket = mock(ConnectionPacket.class);
        when(connectionPacket.getConnectionUuid()).thenReturn(connectionUUID);

        World world = mock(World.class);
        when(game.getWorld()).thenReturn(world);
        when(world.getMyPlayer()).thenReturn(mock(Player.class));

        // Act
        clientPacketListeners.onConnection(connectionPacket);

        // Assert
        verify(gameController, times(1)).handleJoin(connectionUUID);
    }

    @Test
    void testOnConnection_OwnPlayerNotSet() {
        // Arrange
        UUID connectionUUID = UUID.randomUUID();
        ConnectionPacket connectionPacket = mock(ConnectionPacket.class);
        when(connectionPacket.getConnectionUuid()).thenReturn(connectionUUID);

        World world = mock(World.class);
        when(game.getWorld()).thenReturn(world);
        when(world.getMyPlayer()).thenReturn(null);

        // Act
        clientPacketListeners.onConnection(connectionPacket);

        // Assert
        assertEquals(connectionUUID, clientPacketListeners.getConnectionUUID());
    }

    @Test
    void testSendAgentConfigToHost() {
        // Arrange
        UUID connectionUUID = UUID.randomUUID();
        ConnectionPacket connectionPacket = mock(ConnectionPacket.class);
        when(connectionPacket.getUuid()).thenReturn(connectionUUID);
        AgentConfigurationText agentConfig = new AgentConfigurationText("title", "config");

        World world = mock(World.class);
        when(game.getWorld()).thenReturn(world);
        when(world.getMyAgentConfiguration()).thenReturn(agentConfig);

        // Act
        clientPacketListeners.sendAgentConfigToHost(connectionPacket);

        // Assert
        ArgumentCaptor<AgentConfigurationPacket> captor = ArgumentCaptor.forClass(AgentConfigurationPacket.class);
        verify(network, times(1)).sendPacketToHost(captor.capture());
        assertEquals(agentConfig, captor.getValue().getAgentConfig());
        assertEquals(connectionUUID, captor.getValue().getTargetUUID());
    }

    @Test
    void testPacketHandler_AgentConfigurationPacket_NewAgent() {
        // Arrange
        UUID targetUUID = UUID.randomUUID();
        String config = "config";
        AgentConfigurationPacket agentConfigurationPacket = mock(AgentConfigurationPacket.class);
        AgentConfigurationText expectedAgent = new AgentConfigurationText(targetUUID.toString(), config);
        when(agentConfigurationPacket.getTargetUUID()).thenReturn(targetUUID);
        when(agentConfigurationPacket.getAgentConfig()).thenReturn(expectedAgent);

        World world = mock(World.class);
        when(game.getWorld()).thenReturn(world);

        List<AgentConfigurationText> agents = new ArrayList<>();
        when(world.getAgents()).thenReturn(agents);

        // Act
        clientPacketListeners.packetHandler(agentConfigurationPacket);

        // Assert
        verify(world, times(1)).getAgents();
        assertEquals(1, agents.size());
        AgentConfigurationText actualAgent = agents.getFirst();
        assertEquals(expectedAgent.getTitle(), actualAgent.getTitle());
        assertEquals(expectedAgent.getConfiguration(), actualAgent.getConfiguration());
    }
    @Test
    void testPacketHandler_AgentConfigurationPacket_ExistingAgent() {
        // Arrange
        UUID targetUUID = UUID.randomUUID();
        String config = "config";
        AgentConfigurationPacket agentConfigurationPacket = mock(AgentConfigurationPacket.class);
        AgentConfigurationText existingAgent = new AgentConfigurationText(targetUUID.toString(), config);
        when(agentConfigurationPacket.getTargetUUID()).thenReturn(targetUUID);
        when(agentConfigurationPacket.getAgentConfig()).thenReturn(existingAgent);

        World world = mock(World.class);
        when(game.getWorld()).thenReturn(world);

        List<AgentConfigurationText> agents = new ArrayList<>();
        agents.add(existingAgent);
        when(world.getAgents()).thenReturn(agents);

        // Act
        clientPacketListeners.packetHandler(agentConfigurationPacket);

        // Assert
        verify(world, times(1)).getAgents();
        assertEquals(1, agents.size());  // Ensure the list still has only one agent
        assertEquals(existingAgent, agents.get(0));  // Ensure the existing agent is unchanged
    }
    @Test
    void testOnDisconnect() {
        // Arrange
        UUID connectionUUID = UUID.randomUUID();
        DisconnectPacket disconnectPacket = mock(DisconnectPacket.class);
        when(disconnectPacket.getConnectionUuid()).thenReturn(connectionUUID);

        // Act
        clientPacketListeners.onDisconnect(disconnectPacket);

        // Assert
        verify(gameController, times(1)).handleLeave(connectionUUID);
    }
    @Test
    void testOnGameStatePacket() {
        // Arrange (Mocking everything relevant to the method's functionality)
        IGame mockGame = mock(IGame.class);
        GameWorldService mockGameWorldService = mock(GameWorldService.class);
        FloorGenerator mockFloorGenerator = mock(FloorGenerator.class);
        UUID connectionUUID;

        // World and Player setup
        World mockWorld = mock(World.class);
        Player mockMyPlayer = mock(Player.class); // Separate mock for "my player"
        Player mockOtherPlayer = mock(Player.class); // Separate mock for "other player"
        Position mockPosition = mock(Position.class);
        Floor mockFloor = mock(Floor.class);

        // Generate Unique UUIDs for the two players
        UUID myPlayerUUID = UUID.randomUUID();
        UUID otherPlayerUUID = UUID.randomUUID();

        // Mock setup
        List<Floor> floors = new ArrayList<>();
        floors.add(mockFloor);

        when(mockGame.getGameWorldService()).thenReturn(mockGameWorldService);
        when(mockGameWorldService.getFloorGenerator()).thenReturn(mockFloorGenerator);

        // Set up UUIDs for the players
        when(mockMyPlayer.getUUID()).thenReturn(myPlayerUUID);
        when(mockOtherPlayer.getUUID()).thenReturn(otherPlayerUUID);

        when(mockMyPlayer.getPosition()).thenReturn(mockPosition);
        when(mockWorld.getMyPlayer()).thenReturn(mockMyPlayer);
        when(mockWorld.getFloors()).thenReturn(floors);
        when(mockFloor.getLevel()).thenReturn(0);

        // Set up the GameState
        GameController mockGameController = mock(GameController.class);
        when(mockGameController.getWorld()).thenReturn(mockWorld);
        GameState mockGameState = mock(GameState.class);
        GameInfo mockGameInfo = mock(GameInfo.class);
        GameConfiguration mockGameConfiguration = mock(GameConfiguration.class);

        // Ensure the mockGameState returns both players
        when(mockGameState.getPlayers()).thenReturn(List.of(mockMyPlayer, mockOtherPlayer));

        when(mockGameState.getGameInfo()).thenReturn(mockGameInfo);
        when(mockGameInfo.getGameConfiguration()).thenReturn(mockGameConfiguration);
        when(mockGameConfiguration.getSeed()).thenReturn(12345L);
        when(mockGameState.getLowestFloorLevel()).thenReturn(1);
        when(mockGameState.getPickedUpItemsPositions()).thenReturn(new ArrayList<>());

        when(mockFloorGenerator.generateFloor(anyInt(), anyInt())).thenReturn(mockFloor);

        GameStatePacket gameStatePacket = mock(GameStatePacket.class);
        when(gameStatePacket.getGameState()).thenReturn(mockGameState);

        when(mockGame.getWorld()).thenReturn(mockWorld);
        when(mockGame.getGameWorldService()).thenReturn(mockGameWorldService);

        // ClientPacketListeners Setup
        ClientPacketListeners clientPacketListeners = new ClientPacketListeners(mockGame, mockGameController, mock(IClient.class));
        connectionUUID = clientPacketListeners.getConnectionUUID();

        // Act
        clientPacketListeners.onGameStatePacket(gameStatePacket);


        // Verify that setOtherPlayer was called with the correct player (mockOtherPlayer)
        verify(mockWorld).setOtherPlayer(mockOtherPlayer);

        // Assert
        verify(mockGame).setGameState(mockGameState);
        verify(mockGame).handleJoin(connectionUUID);
        verify(mockGame).updatePlayers(gameStatePacket);
        verify(mockGame).handleLowestFloorLevelUpdate(gameStatePacket);

        // Additional verifications
        verify(mockWorld).setOtherPlayer(mockOtherPlayer);
        verify(mockGameWorldService, times(2)).updateWorld();
        verify(mockWorld).spawnPlayerOnWalkableTile(any(), anyInt());
        verify(mockPosition).setZ(1); // Verify the position is updated
    }

}
