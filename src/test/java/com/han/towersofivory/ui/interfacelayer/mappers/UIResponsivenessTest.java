package com.han.towersofivory.ui.interfacelayer.mappers;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.ui.businesslayer.services.UIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * QAR2-1: De actie van een speler moet zichtbaar zijn op de TUI binnen 100 ms actieve speeltijd na de invoer van het commando.
 */
class UIResponsivenessTest {
    private UIMapper uiMapper;
    private UIService uiService = mock(UIService.class);
    private World world = mock(World.class);
    private BasePacket packet = mock(BasePacket.class);

    private final long MAX_RESPONSE_TIME = 100;

    @BeforeEach
    void setUp() {
        // Arrange
        uiMapper = new UIMapper(uiService);
    }

    /**
     * This test measures the response time of the UI upon updating. There is a small chance it may fail due to fluctuations in response times.
     */
    @Test
    void testUpdateUIResponseTime() {
        // Act
        long startTime = System.currentTimeMillis();
        uiMapper.updateUI(world, packet);
        long endTime = System.currentTimeMillis();

        // Assert
        long responseTime = endTime - startTime;
        assertTrue(responseTime <= MAX_RESPONSE_TIME, "Update UI took too long: " + responseTime + "ms");
    }
}
