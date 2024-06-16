package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.entities.items.Item;
import com.han.towersofivory.game.businesslayer.worldgeneration.generators.FloorGenerator;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InteractionActionExecutorTest {

    private InteractionActionExecutor executor;

    @Mock
    private Player player;

    @Mock
    private Player target;

    @Mock
    private World world;

    @Mock
    private Floor currentFloor;

    @Mock
    private Floor nextFloor;

    @Mock
    private Position position;

    @Mock
    private FloorGenerator floorGenerator;

    @Mock
    Room room;

    @Mock
    Item item;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = spy(new InteractionActionExecutor(floorGenerator));
    }


    @ParameterizedTest
    @CsvSource({
            "interactup, 1, 0, 1",
            "interactdown, 1, 2, 1",
            "interactright, 2, 1, 1",
            "interactleft, 0, 1, 1",
    })
    void testPerformActionInteract(String cmd, int expectedX, int expectedY, int expectedZ) {
        // Arrange
        when(world.getMyPlayer()).thenReturn(player);
        when(player.getPosition()).thenReturn(position);
        when(position.getX()).thenReturn(1);
        when(position.getY()).thenReturn(1);
        when(position.getZ()).thenReturn(1);



        List<Floor> floorsToReturn = new ArrayList<>();
        floorsToReturn.add(new Floor(50, 30));
        floorsToReturn.add(new Floor(50, 30));
        when(world.getFloors()).thenReturn(floorsToReturn);

        when(world.getFloor(player.getPosition().getZ())).thenReturn(currentFloor);

        // Act
        executor.performAction(player, world, cmd, null);

        // Assert
        ArgumentCaptor<Position> positionCaptor = ArgumentCaptor.forClass(Position.class);
        verify(executor).interact(positionCaptor.capture(), eq(player), eq(world));

        Position capturedPosition = positionCaptor.getValue();
        assertEquals(expectedX, capturedPosition.getX());
        assertEquals(expectedY, capturedPosition.getY());
        assertEquals(expectedZ, capturedPosition.getZ());
    }

    @ParameterizedTest
    @CsvSource({
            "20, 5, 10, 'No damage should be dealt when the attack is weaker than the defense'",
            "20, 10, 5, 'Expected damage should be dealt when the attack is stronger than the defense'",
            "20, 10, 10, 'No damage should be dealt when attack is equal to defense'"
    })
    void testAttack(int initialHp, int attack, int defense, String message){
        // Arrange
        when(player.getAttack()).thenReturn(attack);
        when(target.getDefense()).thenReturn(defense);
        Position interactionPosition = new Position(0, 0, 0);
        when(target.getPosition()).thenReturn(interactionPosition);

        when(target.getHp()).thenAnswer(invocation -> {
            int damage = Math.max(0, attack - defense);
            int currentHp = initialHp - damage;
            return Math.max(0, currentHp);
        });

        List<Player> otherPlayers = new ArrayList<>();
        otherPlayers.add(target);
        when(world.getOtherPlayers()).thenReturn(otherPlayers);

        when(player.getPosition()).thenReturn(position);
        when(world.getMyPlayer()).thenReturn(player);

        when(world.getFloor(player.getPosition().getZ())).thenReturn(currentFloor);



        int damageDealt = Math.max(0, attack - defense);
        int expectedHp = Math.max(0, initialHp - damageDealt);

        // Act
        InteractionActionExecutor handler = new InteractionActionExecutor(floorGenerator);
        handler.interact(interactionPosition, player, world);

        // Assert
        assertEquals(expectedHp, target.getHp(), message);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, 10, 5",
            "2, 2, 2, 20, 10",
            "3, 3, 3, 30, 20",
    })
    void testHandleAttackInteraction(int posX, int posY, int posZ, int playerAttack, int targetDefense) {
        // Arrange
        Position interactionPosition = new Position(posX, posY, posZ);
        when(player.getPosition()).thenReturn(position);
        when(player.getAttack()).thenReturn(playerAttack);
        when(target.getDefense()).thenReturn(targetDefense);
        when(target.getPosition()).thenReturn(interactionPosition);
        when(world.getPlayers()).thenReturn(Arrays.asList(player, target));

        // Act
        executor.handleAttackInteraction(interactionPosition, player, world);

        // Assert
        verify(target).setHp(anyInt());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 0, 1, 1",
            "2, 2, 0, 2, 2",
            "3, 3, 0, 3, 3",
            // Add more sets of parameters if needed
    })
    void testHandleItemsInteraction(int posX, int posY, int posZ, int itemPosY, int itemPosX) {
        // Arrange
        Position interactionPosition = new Position(posX, posY, posZ);
        when(world.getFloor(posZ)).thenReturn(currentFloor);
        when(currentFloor.getRooms()).thenReturn(Collections.singletonList(room));
        List<Item> items = new ArrayList<>(Collections.singletonList(item));

        when(room.getItems()).thenReturn(items);
        when(item.getPosition()).thenReturn(position);
        when(position.getX()).thenReturn(itemPosY);
        when(position.getY()).thenReturn(itemPosX);

        // Act
        executor.handleItemInteraction(interactionPosition, player, world);

        // Assert
        verify(item).addItem(player);
        verify(currentFloor).setCharacter(posY, posX, ASCIIInterfaceCharacters.ROOM.getCharacter());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 0, 1, 2, 2",
            "2, 2, 0, -1, 3, 3",
            "3, 3, 0, 1, 4, 4",
            // Add more sets of parameters if needed
    })
    void testHandleStairsInteraction(int posX, int posY, int posZ, int stairsDirection, int tileX, int tileY) {
        // Arrange
        Position interactionPosition = new Position(posX, posY, posZ);
        when(player.getPosition()).thenReturn(position);
        when(currentFloor.getStairsDirection(any())).thenReturn(stairsDirection);
        when(world.getFloor(posZ)).thenReturn(currentFloor);
        when(world.getFloor(posZ + stairsDirection)).thenReturn(nextFloor);
        when(nextFloor.getStairsTile(anyInt())).thenReturn(new Tile(tileX, tileY));

        // Act
        executor.handleStairsInteraction(interactionPosition, player, world);

        // Assert
        verify(player.getPosition()).setX(tileX);
        verify(player.getPosition()).setY(tileY + 1);
        verify(player.getPosition()).setZ(posZ + stairsDirection);
        verify(world).getFloor(posZ + stairsDirection);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 0, 1, 2, 2",
            "2, 2, 0, 1, 3, 3",
            "3, 3, 0, 1, 4, 4",
    })
    void testHandleStairsInteractionWhenNextFloorIsntGeneratedYet(int posX, int posY, int posZ, int stairsDirection, int tileX, int tileY) {
        // Arrange
        Position interactionPosition = new Position(posX, posY, posZ);
        when(player.getPosition()).thenReturn(position);
        when(currentFloor.getStairsDirection(any())).thenReturn(stairsDirection);
        when(world.getFloor(posZ)).thenReturn(currentFloor);
        when(world.getFloor(posZ + stairsDirection)).thenReturn(null);
        when(floorGenerator.generateFloor(anyLong(), anyInt())).thenReturn(nextFloor);
        when(nextFloor.getStairsTile(anyInt())).thenReturn(new Tile(tileX, tileY));

        // Act
        executor.handleStairsInteraction(interactionPosition, player, world);

        // Assert
        verify(floorGenerator).generateFloor(anyLong(), anyInt());
        verify(world).addFloor(nextFloor);
        verify(player.getPosition()).setX(tileX);
        verify(player.getPosition()).setY(tileY + 1);
        verify(player.getPosition()).setZ(posZ + stairsDirection);
        verify(world).getFloor(posZ + stairsDirection);
    }
}