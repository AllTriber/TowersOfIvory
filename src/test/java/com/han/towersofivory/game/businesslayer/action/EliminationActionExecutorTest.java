package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EliminationActionExecutorTest {
    private EliminationActionExecutor executor;
    private Player player;
    private World world;

    @BeforeEach
    void setUp() {
        executor = new EliminationActionExecutor();
        player = mock(Player.class);
        world = mock(World.class);
    }

    @Test
    void testPerformActionSetHPTo0() {
        ArgumentCaptor<Integer> hpCaptor = ArgumentCaptor.forClass(Integer.class);

        executor.performAction(player, world, "eliminate", new String[]{});

        verify(player).setHp(hpCaptor.capture());

        assertEquals(0, hpCaptor.getValue());
    }

    @Test
    void testPerformActionSetCoordinatesTo000() {
        ArgumentCaptor<Position> positionCaptor = ArgumentCaptor.forClass(Position.class);

        executor.performAction(player, world, "eliminate", new String[]{});

        verify(player).setPosition(positionCaptor.capture());

        assertEquals(new Position(0, 0, 0), positionCaptor.getValue());
    }

    @Test
    void testPerformActionSetCoordinatesAndHP() {
        ArgumentCaptor<Position> positionCaptor = ArgumentCaptor.forClass(Position.class);
        ArgumentCaptor<Integer> hpCaptor = ArgumentCaptor.forClass(Integer.class);

        executor.performAction(player, world, "eliminate", new String[]{});

        verify(player).setPosition(positionCaptor.capture());
        verify(player).setHp(hpCaptor.capture());

        assertEquals(new Position(0, 0, 0), positionCaptor.getValue());
        assertEquals(0, hpCaptor.getValue());
    }

    @Test
    void testSpecificCheckAction() {
        assertTrue(executor.specificCheckAction(player, world, "eliminate", new String[]{}));
    }
}