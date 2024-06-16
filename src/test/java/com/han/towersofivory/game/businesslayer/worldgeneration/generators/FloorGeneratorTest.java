package com.han.towersofivory.game.businesslayer.worldgeneration.generators;


import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FloorGeneratorTest {
    private FloorGenerator generator;

    @BeforeEach
    public void setUp() {
        int floorWidth = 100;
        int floorHeight = 100;
        int minRoomSize = 10;
        int minRoomHeight = 15;
        generator = new FloorGenerator(floorWidth, floorHeight, minRoomSize, minRoomHeight);
    }

    @Test
    void testGenerateFloor() {
        // Arrange
        int floorNumber = 123;

        // Act
        Floor generatedFloor = generator.generateFloor(floorNumber, 0);

        // Assert
        assertNotNull(generatedFloor, "The method should return a Floor");
    }
}
