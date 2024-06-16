package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
    private World world;
    private FloorGenerator floorGenerator;
    private final long seed = 123;

    @BeforeEach
    public void setUp() {
        world = new World();
        floorGenerator = new FloorGenerator(50, 50, 10, 20);
        world.addFloor(floorGenerator.generateFloor(seed, 0));
        world.setMyPlayer(new Player(UUID.randomUUID()));
    }

    @Test
    void playerSpawnedOnWalkableTile() {
        //Arrange
        Player player = world.getMyPlayer();

        //Act
        world.spawnPlayerOnWalkableTile(player, 0);
        Position playerPosition = player.getPosition();

        //Assert
        assertTrue(world.getFloor(0).isWalkable(playerPosition.getX(), playerPosition.getY()));
    }

    @Test
    void testAddFloor(){
        //Arrange
        for(int i = 1; i < 10; i++){
            world.addFloor(floorGenerator.generateFloor(seed, i));
        }
        Player otherPlayer = new Player(UUID.randomUUID());
        otherPlayer.setHp(10);
        otherPlayer.getPosition().setZ(0);

        world.addOtherPlayer(otherPlayer);

        world.getMyPlayer().setHp(10);
        world.getMyPlayer().getPosition().setZ(1);

        //Act
        world.addFloor(floorGenerator.generateFloor(seed, 10));

        //Assert
        assertEquals(0, otherPlayer.getHp());
        assertEquals(10, world.getMyPlayer().getHp());
        assertEquals(10, world.getFloors().size());
    }

    @Test
    void testGetHighestFloorLevel() {
        // Arrange
        world.addFloor(floorGenerator.generateFloor(seed, 0));
        world.addFloor(floorGenerator.generateFloor(seed, 1));
        world.addFloor(floorGenerator.generateFloor(seed, 2));

        // Act
        int highestFloorLevel = world.getHighestFloorLevel();

        // Assert
        assertEquals(2, highestFloorLevel);
    }
}
