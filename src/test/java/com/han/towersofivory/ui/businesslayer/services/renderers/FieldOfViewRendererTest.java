package com.han.towersofivory.ui.businesslayer.services.renderers;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Floor;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.server.TCPServer;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldOfViewRendererTest {
  private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);

  @Mock
  private World world;

  @Mock
  private Player player;

  @Mock
  private Floor floor;

  private FieldOfViewRenderer fieldOfViewRenderer;

  @BeforeEach
  void setUp() {
    try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
      fieldOfViewRenderer = new FieldOfViewRenderer(24, 12);
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  @Test
  void testRender() {
    // Arrange
    Position playerPosition = new Position(5, 5, 0);
    when(player.getPosition()).thenReturn(playerPosition);
    when(world.getMyPlayer()).thenReturn(player);
    when(world.getFloors()).thenReturn(Collections.singletonList(floor));
    when(floor.getWidth()).thenReturn(10);
    when(floor.getHeight()).thenReturn(10);
    when(floor.isWalkable(anyInt(), anyInt())).thenReturn(true);

    // Act
    String result = fieldOfViewRenderer.render(world);

    // Assert
    assertNotNull(result);
    assertTrue(result.contains(String.valueOf(ASCIIInterfaceCharacters.PLAYER.getCharacter())));
  }
}
