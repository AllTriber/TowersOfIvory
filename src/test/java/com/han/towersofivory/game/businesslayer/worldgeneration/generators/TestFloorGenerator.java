package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFloorGenerator {

    @Test
    void generateFloorCreatesFloorWithDoors() {
        // Arrange
        FloorGenerator floorGenerator = new FloorGenerator(15, 15, 5, 6);

        // Act
        Floor floor = floorGenerator.generateFloor(System.currentTimeMillis(), 1);

        // Assert
        assertNotNull(floor);
        assertTrue(floor.getRooms().stream().anyMatch(room -> room.getDoors().size() > 0),
                "At least one room should have doors");
    }

    @Test
    void generateFloorCreatesFloorWithStairs() {
        // Arrange
        FloorGenerator floorGenerator = new FloorGenerator(15, 15, 5, 6);

        // Act
        Floor floor = floorGenerator.generateFloor(System.currentTimeMillis(), 1);

        // Assert
        assertNotNull(floor);
        assertNotNull(floor.getStairsTile(1), "Stairs should be placed");
    }

    @Test
    void removeDeadEndsRemovesDeadEnds() {
        // Arrange
        FloorGenerator floorGenerator = new FloorGenerator(15, 15, 5, 6);
        Floor floor = floorGenerator.generateFloor(System.currentTimeMillis(), 1);

        // Act
        floorGenerator.removeDeadEnds();

        // Assert
        for (int row = 0; row < floor.getHeight(); row++) {
            for (int col = 0; col < floor.getWidth(); col++) {
                assertNotEquals('X', floor.getAsciiCharacterOfTile(new Tile(col, row)), "Dead ends should be removed");
            }
        }
    }
}
