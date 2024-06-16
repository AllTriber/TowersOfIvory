package com.han.towersofivory.game.interfacelayer.mappers;

import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.Gamemode;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.worldgeneration.GameWorldService;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.ui.interfacelayer.mappers.UIMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameMapperTest {
    @Mock
    private UIMapper uiMapper;
    private GameMapper gameMapper;
    private GameController gameController;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        gameMapper = new GameMapper(uiMapper);

        gameController = Mockito.spy(new GameController(gameMapper));

        Field gameControllerField = GameMapper.class.getDeclaredField("gameController");
        gameControllerField.setAccessible(true);
        gameControllerField.set(gameMapper, gameController);
    }

    @Test
    void createGameCallsGameController() throws ConnectionFailedException {
        //Arrange
        GameConfiguration gameConfiguration = new GameConfiguration("test", Gamemode.LMS, 123);
        GameInfo gameInfo = new GameInfo(gameConfiguration, 1);
        HostInformation hostInformation = Mockito.mock(HostInformation.class);

        when(gameController.createGame(gameInfo)).thenReturn(hostInformation);

        //Act
        HostInformation result = gameMapper.createGame(gameInfo);

        //Assert
        verify(gameController).createGame(gameInfo);
        assertEquals(hostInformation, result);
    }

    @Test
    void stopGameCallsGameController() {
        //Arrange/Act
        gameMapper.stopGame();

        //Assert
        verify(gameController).stopGame();
    }

    @Test
    void getHostsCallsGameController() {
        //Arrange
        List<HostInformation> hosts = Mockito.mock(List.class);
        when(gameController.searchForHosts()).thenReturn(hosts);

        //Act
        List<HostInformation> result = gameMapper.getHosts();

        //Assert
        verify(gameController).searchForHosts();
        assertEquals(hosts, result);
    }

    @Test
    void sendInputCallsGameController() {
        //Arrange
        String input = "test";

        //Act
        gameMapper.sendInput(input);

        //Assert
        verify(gameController).sendInput(input);
    }

    @Test
    void getGameStateCallsGameController() {
        //Arrange
        gameMapper.generateWorld(Mockito.mock(GameConfiguration.class));
        gameController.setGameState(Mockito.mock(GameState.class));

        GameState gameState = Mockito.mock(GameState.class);
        when(gameController.getGameState()).thenReturn(gameState);

        //Act
        GameState result = gameMapper.getGameState();

        //Assert
        verify(gameController).getGameState();
        assertEquals(gameState, result);
    }

    @Test
    void setGameStateCallsGameController() {
        //Arrange
        GameState gameState = Mockito.mock(GameState.class);

        //Act
        gameMapper.setGameState(gameState);

        //Assert
        verify(gameController).setGameState(gameState);
    }


    @Test
    void getsPlayers() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        UUID uuid = new UUID(10, 10);
        ArrayList<Player> players = new ArrayList<>();
        Player expectedPlayer = new Player(uuid);
        players.add(expectedPlayer);

        World world = Mockito.mock(World.class);
        when(world.getPlayers()).thenReturn(players);

        GameWorldService gameWorldService = Mockito.mock(GameWorldService.class);
        when(gameWorldService.getWorld()).thenReturn(world);

        Field field = GameMapper.class.getDeclaredField("worldService");
        field.setAccessible(true);
        field.set(gameMapper, gameWorldService);

        // Act
        Player retrievedPlayer = gameMapper.getplayer(uuid);

        // Assert
        assertEquals(expectedPlayer, retrievedPlayer);
    }

}
