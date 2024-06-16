package com.han.towersofivory.game.businesslayer.entities;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {
    private Player sut;
    private UUID mockedUUID;


    @BeforeEach
    void setUp() {
        mockedUUID = UUID.randomUUID();
        sut = spy(new Player(mockedUUID));
    }

    @Test
    void TestGetUUID() {
        // Arrange
        UUID expected = mockedUUID;

        // Act
        UUID actual = sut.getUUID();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void TestsetUUID() {
        // Arrange
        UUID expected = UUID.randomUUID();

        // Act
        sut.setUUID(expected);
        UUID actual = sut.getUUID();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void TestToString() {
        // Arrange
        String expected = "Player{" +
                "uuid=" + mockedUUID +
                '}';

        // Act
        String actual = sut.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void TestUpdatePlayerVisionInRoomWithPlayerVisionNull() {
        // Arrange

        // Setup mocked objects
        Position mockedPosition = mock(Position.class);
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom1 = mock(Room.class);
        Room mockedRoom2 = mock(Room.class);
        mockedRooms.add(mockedRoom1);
        mockedRooms.add(mockedRoom2);
        String lastCommand = "left";

        // Setup mockedWorld to return mockedRooms when getCurrentFloor().getRooms() is called
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);

        // Setup room positions that will be returned by getX(), getY(), getWidth(), and getHeight() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getX()).thenReturn(0);
        when(mockedRoom1.getY()).thenReturn(0);
        when(mockedRoom1.getWidth()).thenReturn(5);
        when(mockedRoom1.getHeight()).thenReturn(5);
        when(mockedRoom2.getX()).thenReturn(7);
        when(mockedRoom2.getY()).thenReturn(7);
        when(mockedRoom2.getWidth()).thenReturn(5);
        when(mockedRoom2.getHeight()).thenReturn(5);

        // Setup player position that will be returned by getPosition()
        sut.setPosition(mockedPosition);
        when(mockedPosition.getX()).thenReturn(2);
        when(mockedPosition.getY()).thenReturn(2);
        when(mockedPosition.getZ()).thenReturn(0);

        // Setup door positions that will be returned by getX() and getY() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getDoors()).thenReturn(List.of(new Tile(0, 3), new Tile(3, 0)));
        when(mockedRoom2.getDoors()).thenReturn(List.of(new Tile(7, 9), new Tile(9, 7)));

        // Act
        sut.updatePlayerVision(mockedWorld, lastCommand);

        // Assert
        assertEquals(mockedRoom1, sut.getPlayerVision().getRoom());
    }

    @Test
    void TestUpdatePlayerVisionInRoomWithPlayerVisionTheSameAsCurrect() {
        // Arrange

        // Setup mocked objects
        Position mockedPosition = mock(Position.class);
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom1 = mock(Room.class);
        Room mockedRoom2 = mock(Room.class);
        mockedRooms.add(mockedRoom1);
        mockedRooms.add(mockedRoom2);
        String command1 = "left";
        String command2 = "right";

        // Setup mockedWorld to return mockedRooms when getCurrentFloor().getRooms() is called
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);

        // Setup room positions that will be returned by getX(), getY(), getWidth(), and getHeight() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getX()).thenReturn(0);
        when(mockedRoom1.getY()).thenReturn(0);
        when(mockedRoom1.getWidth()).thenReturn(5);
        when(mockedRoom1.getHeight()).thenReturn(5);
        when(mockedRoom2.getX()).thenReturn(7);
        when(mockedRoom2.getY()).thenReturn(7);
        when(mockedRoom2.getWidth()).thenReturn(5);
        when(mockedRoom2.getHeight()).thenReturn(5);

        // Setup player position that will be returned by getPosition()
        sut.setPosition(mockedPosition);
        when(mockedPosition.getX()).thenReturn(2);
        when(mockedPosition.getY()).thenReturn(2);
        when(mockedPosition.getZ()).thenReturn(0);

        // Setup door positions that will be returned by getX() and getY() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getDoors()).thenReturn(List.of(new Tile(0, 3), new Tile(3, 0)));
        when(mockedRoom2.getDoors()).thenReturn(List.of(new Tile(7, 9), new Tile(9, 7)));

        // Act
        sut.updatePlayerVision(mockedWorld, command1);
        sut.updatePlayerVision(mockedWorld, command2);

        // Assert
        assertEquals(command2, sut.getPlayerVision().getLastMovement());
    }

    @Test
    void TestUpdatePlayerVisionInRoomOnDoorWithPlayerVisionNull() {
        // Arrange

        // Setup mocked objects
        Position mockedPosition = mock(Position.class);
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom1 = mock(Room.class);
        Room mockedRoom2 = mock(Room.class);
        mockedRooms.add(mockedRoom1);
        mockedRooms.add(mockedRoom2);
        String command1 = "left";
        String command2 = "right";

        // Setup mockedWorld to return mockedRooms when getCurrentFloor().getRooms() is called
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);

        // Setup room positions that will be returned by getX(), getY(), getWidth(), and getHeight() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getX()).thenReturn(0);
        when(mockedRoom1.getY()).thenReturn(0);
        when(mockedRoom1.getWidth()).thenReturn(5);
        when(mockedRoom1.getHeight()).thenReturn(5);
        when(mockedRoom2.getX()).thenReturn(7);
        when(mockedRoom2.getY()).thenReturn(7);
        when(mockedRoom2.getWidth()).thenReturn(5);
        when(mockedRoom2.getHeight()).thenReturn(5);

        // Setup player position that will be returned by getPosition()
        sut.setPosition(mockedPosition);
        when(mockedPosition.getX()).thenReturn(3);
        when(mockedPosition.getY()).thenReturn(0);
        when(mockedPosition.getZ()).thenReturn(0);

        // Setup door positions that will be returned by getX() and getY() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getDoors()).thenReturn(List.of(new Tile(0, 3), new Tile(3, 0)));
        when(mockedRoom2.getDoors()).thenReturn(List.of(new Tile(7, 9), new Tile(9, 7)));

        // Act
        sut.updatePlayerVision(mockedWorld, command1);
        sut.updatePlayerVision(mockedWorld, command2);

        // Assert
        assertEquals(command2, sut.getPlayerVision().getLastMovement());
    }

    @Test
    void TestUpdatePlayVisionInCorridorWithPlayVisionNull() {
        // Arrange

        // Setup mocked objects
        Position mockedPosition = mock(Position.class);
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom1 = mock(Room.class);
        Room mockedRoom2 = mock(Room.class);
        mockedRooms.add(mockedRoom1);
        mockedRooms.add(mockedRoom2);
        String command = "left";

        // Setup mockedWorld to return mockedRooms when getCurrentFloor().getRooms() is called
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);

        // Setup room positions that will be returned by getX(), getY(), getWidth(), and getHeight() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getX()).thenReturn(0);
        when(mockedRoom1.getY()).thenReturn(0);
        when(mockedRoom1.getWidth()).thenReturn(5);
        when(mockedRoom1.getHeight()).thenReturn(5);
        when(mockedRoom2.getX()).thenReturn(7);
        when(mockedRoom2.getY()).thenReturn(7);
        when(mockedRoom2.getWidth()).thenReturn(5);
        when(mockedRoom2.getHeight()).thenReturn(5);

        // Setup player position that will be returned by getPosition()
        sut.setPosition(mockedPosition);
        when(mockedPosition.getX()).thenReturn(6);
        when(mockedPosition.getY()).thenReturn(6);
        when(mockedPosition.getZ()).thenReturn(0);

        // Setup door positions that will be returned by getX() and getY() for mockedRoom1 and mockedRoom2
        when(mockedRoom1.getDoors()).thenReturn(List.of(new Tile(0, 3), new Tile(3, 0)));
        when(mockedRoom2.getDoors()).thenReturn(List.of(new Tile(7, 9), new Tile(9, 7)));

        // Act
        sut.updatePlayerVision(mockedWorld, command);

        // Assert
        assertNull(sut.getPlayerVision().getRoom());
    }

    @Test
    void TestGetPlayerVision() {
        // Arrange
        PlayerVision expected = new PlayerVision(null, false, "left");
        sut.setPlayerVision(expected);

        // Act
        PlayerVision actual = sut.getPlayerVision();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void TestCheckIfOnDoorTile() {
        // Arrange
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom = mock(Room.class);
        mockedRooms.add(mockedRoom);
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);
        when(mockedRoom.getDoors()).thenReturn(List.of(new Tile(2, 2)));

        // Act
        boolean actual = sut.checkIfOnDoorTile(mockedWorld, 2, 2, 0);

        // Assert
        assertTrue(actual);
    }

    @Test
    void TestShouldUpdateVision() {
        // Arrange
        boolean isOnDoorTile = true;
        boolean zLevelChanged = true;

        // Act
        boolean actual = sut.shouldUpdateVision(isOnDoorTile, zLevelChanged);

        // Assert
        assertTrue(actual);
    }

    @Test
    void TestUpdateOtherPlayersInRoom() {
        // Arrange
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        Room mockedRoom = mock(Room.class);
        Position mockedPosition = mock(Position.class);
        Player otherPlayer = mock(Player.class); // Ensure otherPlayer is also a mock
        ArrayList<Player> otherPlayers = new ArrayList<>();
        otherPlayers.add(otherPlayer);

        when(mockedWorld.getOtherPlayers()).thenReturn(otherPlayers);
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(new ArrayList<>(Arrays.asList(mockedRoom)));  // Use Arrays.asList to return a mutable list
        when(mockedRoom.getX()).thenReturn(0);
        when(mockedRoom.getY()).thenReturn(0);
        when(mockedRoom.getWidth()).thenReturn(5);
        when(mockedRoom.getHeight()).thenReturn(5);
        when(otherPlayer.getPosition()).thenReturn(mockedPosition);
        when(mockedPosition.getX()).thenReturn(2);
        when(mockedPosition.getY()).thenReturn(2);
        when(mockedPosition.getZ()).thenReturn(0);
        when(sut.getPosition()).thenReturn(mockedPosition);  // Use spy to stub getPosition()

        // Ensure the player's vision is initialized correctly
        sut.updatePlayerVision(mockedWorld, "left");

        // Act
        sut.updateOtherPlayersInRoom(mockedWorld);

        // Assert
        assertEquals(1, sut.getPlayerVision().getObjectsInRoom().get("otherPlayers"));
    }

    @Test
    void TestUpdateObjectsInRoom() {
        // Arrange
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom = mock(Room.class);
        mockedRooms.add(mockedRoom);
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);
        when(mockedRoom.getDoors()).thenReturn(List.of(new Tile(2, 2), new Tile(3, 3)));
        sut.setPlayerVision(new PlayerVision(mockedRoom, true, "left"));

        // Act
        sut.updateObjectsInRoom(mockedWorld);

        // Assert
        assertEquals(2, sut.getPlayerVision().getObjectsInRoom().get("doors"));
    }

    @Test
    void TestGetRoomForPlayer() {
        // Arrange
        World mockedWorld = mock(World.class);
        Floor mockedFloor = mock(Floor.class);
        ArrayList<Room> mockedRooms = new ArrayList<>();
        Room mockedRoom = mock(Room.class);
        mockedRooms.add(mockedRoom);
        when(mockedWorld.getFloor(sut.getPosition().getZ())).thenReturn(mockedFloor);
        when(mockedFloor.getRooms()).thenReturn(mockedRooms);
        when(mockedRoom.getX()).thenReturn(0);
        when(mockedRoom.getY()).thenReturn(0);
        when(mockedRoom.getWidth()).thenReturn(5);
        when(mockedRoom.getHeight()).thenReturn(5);
        Player player = new Player(UUID.randomUUID());
        player.setPosition(new Position(2, 2, 0));

        // Act
        Room actual = sut.getRoomForPlayer(player, mockedWorld);

        // Assert
        assertEquals(mockedRoom, actual);
    }
}