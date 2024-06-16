package com.han.towersofivory.game.businesslayer.worldgeneration.generators;

import com.han.towersofivory.game.businesslayer.entities.items.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * UC2.2.2 - Item oprapen
 * Generates random list of items.
 */
public class ItemGenerator {
    Random random;

    public ItemGenerator(Random random) {
        this.random = random;
    }

    /**
     * Generates a list of random items.
     * @param amount The amount of items to generate.
     * @return A list of items.
     */
    public List<Item> generateItems(int amount) {
        List<Item> items = new ArrayList<>();
        List<Supplier<Item>> possibleItems = Arrays.asList(
                GoldPlating::new,
                () -> new Chest(random.nextInt(3)),
                Absence::new,
                C4Model::new);

        for(int i = 0; i < amount; i++) {
            int randomIndex = random.nextInt(possibleItems.size());
            Supplier<Item> itemSupplier = possibleItems.get(randomIndex);
            Item newItem = itemSupplier.get();
            items.add(newItem);
        }

        return items;
    }
}
