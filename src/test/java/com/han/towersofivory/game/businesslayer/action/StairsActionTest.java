package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StairsActionTest {
    private InteractionActionExecutor SUT;
    private World world = mock(World.class);
    private Player player = mock(Player.class);
    private Floor currentFloor = mock(Floor.class);
    private Floor nextFloor = mock(Floor.class);
    private FloorGenerator floorGenerator = mock(FloorGenerator.class);
    private Position playerPosition;
    private Position stairsPosition;

    @BeforeEach
    void setUp() {
        // Arrange
        SUT = new InteractionActionExecutor(floorGenerator);
        playerPosition = new Position(1, 1, 0);
        stairsPosition = new Position(5, 5, 0);

        when(world.getFloor(0)).thenReturn(currentFloor);
        when(world.getFloor(1)).thenReturn(nextFloor);
        when(player.getPosition()).thenReturn(playerPosition);
        when(currentFloor.getStairsDirection(any(Tile.class))).thenReturn(1);
        when(nextFloor.getStairsTile(-1)).thenReturn(new Tile(stairsPosition.getX(), stairsPosition.getY()));
    }

    @Test
    void testStairsActionNoNextFloorGenerateNewFloor() {
        // Arrange
        when(world.getFloor(1)).thenReturn(null);
        when(floorGenerator.generateFloor(anyLong(), eq(1))).thenReturn(nextFloor);

        // Act
        SUT.handleStairsInteraction(stairsPosition, player, world);

        // Assert
        verify(floorGenerator).generateFloor(anyLong(), eq(1));
        verify(world).addFloor(nextFloor);
    }

    @Test
    void testStairsActionMovePlayerToNextFloor() {
        // Act
        SUT.handleStairsInteraction(stairsPosition, player, world);

        // Assert
        Position expectedNewPosition = new Position(5, 6, 1);
        Position playerPosition = player.getPosition();

        assertEquals(expectedNewPosition.getX(), playerPosition.getX());
        assertEquals(expectedNewPosition.getY(), playerPosition.getY());
        assertEquals(expectedNewPosition.getZ(), playerPosition.getZ());

    }
}