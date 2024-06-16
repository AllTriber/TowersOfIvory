package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.entities.items.Item;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ItemGeneratorTest {
    @Test
    void generateItemsReturnsTheCorrectAmountOfRequestedItems() {
        // Arrange
        ItemGenerator SUT = new ItemGenerator(new Random(123));
        int amount = 3;

        // Act
        List<Item> result = SUT.generateItems(amount);

        // Assert
        assertEquals(amount, result.size(), "The size of the generated items list should be equal to the requested amount");
    }
}