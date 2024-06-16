package com.han.towersofivory.ui.businesslayer.services;

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

import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ASCIIWorldServiceTest {
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);

    @Mock
    private World world;

    @Mock
    private Player player;

    @Mock
    private Floor floor;

    private ASCIIWorldService asciiWorldService;

    @BeforeEach
    void setUp() {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            asciiWorldService = new ASCIIWorldService();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private String expectedRenderedWorld() {
        StringBuilder sb = new StringBuilder();

        for (int y = 35; y < 65; y++) {
            for (int x = 35; x < 65; x++) {
                if (x == 50 && y == 50) {
                    sb.append(ASCIIInterfaceCharacters.PLAYER.getCharacter());
                } else if (x == 52 && y == 52) {
                    sb.append(ASCIIInterfaceCharacters.OTHERPLAYER.getCharacter());
                } else {
                    sb.append('.');
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
