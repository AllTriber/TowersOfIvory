package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class MoveActionExecutorTest {

    private final Player actingPlayer = mock(Player.class);
    private final Player otherPlayer = mock(Player.class);
    private final World world = mock(World.class);
    private final Floor currentfloor = mock(Floor.class);
    private MoveActionExecutor SUT;

    static Stream<Arguments> provideWalkableTiles() {
        return Arrays.stream(toCharacterArray(ASCIIInterfaceCharacters.getWalkableCharacters()))
                .map(Arguments::of);
    }

    static Stream<Arguments> provideUnwalkableTiles() {
        return Arrays.stream(toCharacterArray(ASCIIInterfaceCharacters.getUnwalkableCharacters()))
                .map(Arguments::of);
    }

    /**
     * Converts a char array to a Character array.
     *
     * @param cArray the char array to convert
     * @return the Character array
     */
    private static Character[] toCharacterArray(char[] cArray) {
        Character[] CArray = new Character[cArray.length];
        for (int i = 0; i < cArray.length; i++) {
            CArray[i] = cArray[i];
        }
        return CArray;
    }

    @BeforeEach
    void setUp() {
        SUT = new MoveActionExecutor();
    }

    @ParameterizedTest
    @CsvSource({
            "right, 1, 0, 0",
            "east, 1, 0, 0",
            "left, -1, 0, 0",
            "west, -1, 0, 0",
            "up, 0, -1, 0",
            "north, 0, -1, 0",
            "down, 0, 1, 0",
            "south, 0, 1, 0"
    })
    void performActionPlayerMoves(String cmd, int expectedX, int expectedY, int expectedZ) {
        // Arrange
        String[] args = new String[0];
        ArrayList<Player> PlayersList = new ArrayList<>();
        PlayersList.add(actingPlayer);
        PlayersList.add(otherPlayer);

        when(actingPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(otherPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(actingPlayer.getPosition()).thenReturn(new Position(0, 0, 0));
        when(world.getOtherPlayers()).thenReturn(PlayersList);

        // Act
        SUT.performAction(actingPlayer, world, cmd, args);

        // Assert
        Assertions.assertEquals(expectedX, actingPlayer.getPosition().getX());
        Assertions.assertEquals(expectedY, actingPlayer.getPosition().getY());
        Assertions.assertEquals(expectedZ, actingPlayer.getPosition().getZ());
    }

    @ParameterizedTest
    @MethodSource("provideWalkableTiles")
    void checkActionPlayerCanMoveToEmptyTile(Character c) {
        // Arrange
        String cmd = "right";
        String[] args = new String[0];
        actingPlayer.setHp(10);
        ArrayList<Player> PlayersList = new ArrayList<>();
        PlayersList.add(actingPlayer);
        PlayersList.add(otherPlayer);

        when(world.getFloor(0)).thenReturn(currentfloor);
        when(actingPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(otherPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(actingPlayer.getPosition()).thenReturn(new Position(0, 0, 0));
        when(otherPlayer.getPosition()).thenReturn(new Position(3, 3, 0));
        when(currentfloor.getAsciiCharacterOfTile(any())).thenReturn(c);
        when(world.getOtherPlayers()).thenReturn(PlayersList);

        // Act
        boolean result = SUT.specificCheckAction(actingPlayer, world, cmd, args);

        // Assert
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("provideUnwalkableTiles")
    void checkActionPlayerCannotMoveToOccupiedTile(Character c) {
        // Arrange
        String cmd = "right";
        String[] args = new String[0];
        ArrayList<Player> PlayersList = new ArrayList<>();
        PlayersList.add(actingPlayer);
        PlayersList.add(otherPlayer);

        when(world.getFloor(0)).thenReturn(currentfloor);
        when(actingPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(otherPlayer.getUUID()).thenReturn(UUID.randomUUID());
        when(actingPlayer.getPosition()).thenReturn(new Position(0, 0, 0));
        when(otherPlayer.getPosition()).thenReturn(new Position(1, 0, 0));
        when(currentfloor.getAsciiCharacterOfTile(any())).thenReturn(c);
        when(world.getOtherPlayers()).thenReturn(PlayersList);

        // Act
        boolean result = SUT.specificCheckAction(actingPlayer, world, cmd, args);

        System.out.println(result);
        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void toCharacterArrayTest() {
        // Arrange
        char[] cArray = {'a', 'b', 'c'};
        Character[] expected = {'a', 'b', 'c'};

        // Act
        Character[] result = toCharacterArray(cArray);

        // Assert
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void toCharacterArrayTestEmpty() {
        // Arrange
        char[] cArray = {};
        Character[] expected = {};

        // Act
        Character[] result = toCharacterArray(cArray);

        // Assert
        Assertions.assertArrayEquals(expected, result);
    }

}
