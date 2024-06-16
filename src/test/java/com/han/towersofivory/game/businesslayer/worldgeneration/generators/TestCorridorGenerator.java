package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Room;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class TestCorridorGenerator {

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void corridorsAreCreatedInTheDungeon() {
        // Arrange
        Floor floor = new Floor(10, 10); // Assuming Floor constructor takes width and height
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Act
        corridorGenerator.createCorridors();

        // Assert
        boolean hasCorridors = false;

        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                Tile tile = new Tile(col, row); // Note: Tile constructor takes (x, y)
                if (floor.getAsciiCharacterOfTile(tile) == ASCIIInterfaceCharacters.CORRIDOR.getCharacter()) {
                    hasCorridors = true;
                    break;
                }
            }
            if (hasCorridors) {
                break;
            }
        }
        assertTrue(hasCorridors, "The dungeon should have corridors after calling createCorridors()");
    }

    @Test
    void corridorsAreAdjacentToRoom() {
        // Arrange
        Floor floor = new Floor(10, 10); // Assuming Floor constructor takes width and height
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Add a room to the placedRooms list
        Room room = new Room(2, 2);
        room.setY(2);
        room.setX(2);
        floor.addRoom(room);

        // Manually place a corridor next to the room
        floor.setCharacter(4, 2, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());

        // Act
        List<List<Tile>> corridorsAdjacentToRoom = corridorGenerator.findCorridorsAdjacentToRoom(floor.getRooms());

        // Assert
        assertNotNull(corridorsAdjacentToRoom, "The method should return a non-null result");
        assertFalse(corridorsAdjacentToRoom.isEmpty(), "The method should return a non-empty list");
        assertFalse(corridorsAdjacentToRoom.getFirst().isEmpty(), "The list of corridors adjacent to the room should not be empty");
    }

    @Test
    void removeInaccessibleTilesRemovesInaccessibleTiles() {
        // Arrange
        Floor floor = new Floor(3, 1);
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Place some walkable tiles
        floor.setCharacter(0, 0, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());
        floor.setCharacter(0, 2, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());

        // Mark some tiles as inaccessible
        floor.setCharacter(0, 1, ASCIIInterfaceCharacters.SPACE.getCharacter());

        // Ensure getRandomWalkableTile always returns the same tile
        when(random.nextInt(anyInt())).thenReturn(0);

        // Act
        corridorGenerator.removeInaccessibleTiles();

        // Assert
        assertEquals(' ', floor.getAsciiCharacterOfTile(new Tile(2, 0)), "The inaccessible tiles should be removed");
    }


    @Test
    void removeInaccessibleTilesHandlesNoWalkableTiles() {
        // Arrange
        Floor floor = new Floor(5, 5);
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Act
        corridorGenerator.removeInaccessibleTiles();

        // Assert
        assertEquals(0, floor.getRooms().size(), "No rooms should be present if there are no walkable tiles");
    }

    @Test
    void removeInaccessibleTilesHandlesAllWalkableTiles() {
        // Arrange
        Floor floor = new Floor(5, 5);
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Place all walkable tiles
        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                floor.setCharacter(col, row, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());
            }
        }

        // Act
        corridorGenerator.removeInaccessibleTiles();

        // Assert
        assertEquals(0, floor.getRooms().size(), "No rooms should be present if all tiles are walkable");
    }


    @Test
    void getRandomWalkableTileReturnsNullWhenNoWalkableTiles() {
        // Arrange
        Floor floor = new Floor(10, 10);
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);

        // Act
        Tile randomWalkableTile = corridorGenerator.getRandomWalkableTile();

        // Assert
        assertNull(randomWalkableTile, "The method should return null when there are no walkable tiles");
    }

    @Test
    void getRandomWalkableTileReturnsWalkableTile() {
        // Arrange
        Floor floor = new Floor(10, 10);
        CorridorGenerator corridorGenerator = new CorridorGenerator(floor, random);
        // Make some tiles walkable
        floor.setCharacter(1, 1, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());
        floor.setCharacter(2, 2, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());
        floor.setCharacter(3, 3, ASCIIInterfaceCharacters.CORRIDOR.getCharacter());

        List<Tile> walkableTiles = new ArrayList<>();
        walkableTiles.add(new Tile(1, 1));
        walkableTiles.add(new Tile(2, 2));
        walkableTiles.add(new Tile(3, 3));

        when(random.nextInt(walkableTiles.size())).thenReturn(1); // Return the index of the second walkable tile

        // Act
        Tile randomWalkableTile = corridorGenerator.getRandomWalkableTile();

        // Assert
        assertNotNull(randomWalkableTile, "The method should return a non-null walkable tile");

        boolean found = false;
        for (Tile tile : walkableTiles) {
            if (tile.getX() == randomWalkableTile.getX() && tile.getY() == randomWalkableTile.getY()) {
                found = true;
                break;
            }
        }

        assertTrue(found, "The returned tile should be one of the walkable tiles");
        assertEquals(2, randomWalkableTile.getX(), "The returned tile's X coordinate should match the expected value");
        assertEquals(2, randomWalkableTile.getY(), "The returned tile's Y coordinate should match the expected value");
    }
}
