package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.gamesave.GameSaver;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class StopActionExecutorTest {

    private StopActionExecutor sut;

    @Test
    void performActionCallsWorldStopGame() throws IOException {
        // arrange
        var mockedPlayer = Mockito.mock(Player.class);
        var mockedWorld = Mockito.mock(World.class);
        String cmd = "";
        String[] args = {};
        var mockedGameMapper = Mockito.mock(GameMapper.class);
        var mockedGameState = Mockito.mock(GameState.class);
        when(mockedGameMapper.getGameState()).thenReturn(mockedGameState);
        var mockedGameInfo = Mockito.mock(GameInfo.class);
        when(mockedGameState.getGameInfo()).thenReturn(mockedGameInfo);
        var mockedGameCOnfiguration = Mockito.mock(GameConfiguration.class);
        when(mockedGameInfo.getGameConfiguration()).thenReturn(mockedGameCOnfiguration);
        var filename = "name";
        when(mockedGameCOnfiguration.getName()).thenReturn(filename);
        sut = new StopActionExecutor(mockedGameMapper);

        try (var mockedGameSaver = Mockito.mockStatic(GameSaver.class)) {

        // act
        sut.performAction(mockedPlayer, mockedWorld, cmd, args);

        // assert
        verify(mockedWorld).stopGame();
        }
    }

    @Test
    void specificCheckActionComparesUUIDCorrectly(){
        // arrange
        var mockedPlayer = Mockito.mock(Player.class);
        var mockedWorld = Mockito.mock(World.class);
        var cmd = "";
        var args = new String[]{};
        var gameMapper = Mockito.mock(GameMapper.class);
        sut = new StopActionExecutor(gameMapper);

        UUID uuid = UUID.randomUUID();

        when(mockedPlayer.getUUID()).thenReturn(uuid);
        when(mockedWorld.getMyPlayerUUID()).thenReturn(uuid);

        // act
        var actual = sut.specificCheckAction(mockedPlayer , mockedWorld, cmd, args);

        // assert
        assertTrue(actual);
    }

    @Test
    void specificCheckActionDeniesIncorrectAction(){

        // arrange
        var mockedPlayer = Mockito.mock(Player.class);
        var mockedWorld = Mockito.mock(World.class);
        var cmd = "";
        var args = new String[]{};
        var gameMapper = Mockito.mock(GameMapper.class);
        sut = new StopActionExecutor(gameMapper);

        UUID uuid = UUID.randomUUID();
        UUID wrongUUID = UUID.randomUUID();

        when(mockedPlayer.getUUID()).thenReturn(uuid);
        when(mockedWorld.getMyPlayerUUID()).thenReturn(wrongUUID);

        // act
        var actual = sut.specificCheckAction(mockedPlayer , mockedWorld, cmd, args);

        // assert
        assertFalse(actual);
    }

}

