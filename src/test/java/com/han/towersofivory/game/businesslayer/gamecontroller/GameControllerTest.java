package com.han.towersofivory.game.businesslayer.gamecontroller;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
import com.han.towersofivory.network.interfacelayer.interfaces.IHost;
import com.han.towersofivory.network.interfacelayer.mappers.NetworkMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Captor
    private ArgumentCaptor<ActionPacket> actionPacketCaptor;

    @Mock
    private NetworkMapper mockNetwork;

    @Mock
    private IGame mockGame;

    @Mock
    private IHost mockHost;

    @Mock
    private IClient mockClient;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        mockNetwork = mock(NetworkMapper.class);
        mockGame = mock(IGame.class);
        mockHost = mock(IHost.class);
        mockClient = mock(IClient.class);
        gameController = new GameController(mockGame);
    }

    @Test
    void resumeGame_shouldThrowUnsupportedOperationException() {
        // Act & Assert
        // Resume game should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> gameController.resumeGame(mockGame));
    }

    @Test
    void handleActionOnPeer_shouldCallGamePerformAction() throws IOException {
        // Arrange
        UUID playerUUID = UUID.randomUUID();
        String command = "some_command";
        String[] args = {"arg1", "arg2"};

        // Act
        gameController.handleActionOnPeer(playerUUID, command, args);

        // Assert
        verify(mockGame, times(1)).performAction(playerUUID, command, args);
    }
}
