//package com.han.towersofivory.ui.businesslayer.services;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.googlecode.lanterna.screen.Screen;
//import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
//import com.han.towersofivory.game.interfacelayer.interfaces.IGame;
//import com.han.towersofivory.network.businesslayer.packet.BasePacket;
//import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//public class TestUiService {
//
//    @Mock
//    private IGame game;
//
//    @Mock
//    private IClient networkMapper;
//
//    @Mock
//    private SwingTerminalFrame terminalFrame;
//
//    @Mock
//    private Screen screen;
//
//    @Mock
//    private ASCIIWorldService worldService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void updateUI_ShouldAddPacketToStringToUpdatesList() {
//        // Arrange
//        UIService uiService = new UIService();
//        uiService.setGame(game);
//
//        BasePacket packet = mock(BasePacket.class);
//        String packetString = "Packet content";
//        when(packet.toString()).thenReturn(packetString);
//
//        // Act
//        uiService.updateUI(packet);
//
//        // Assert
//        assertEquals(1, uiService.getUpdates().size());
//        assertEquals(packetString, uiService.getUpdates().get(0));
//    }
//
//    @Test
//    void showTUI_ShouldStartScreenWithTerminalFrameAndInitializeGUI() throws IOException {
//        // Arrange
//        UIService uiService = new UIService();
//        uiService.setNetworkMapper(networkMapper);
//        when(uiService.createTerminalFrame()).thenReturn(terminalFrame);
//        when(uiService.createAndStartScreen(terminalFrame)).thenReturn(screen);
//
//        // Act
//        uiService.showTUI();
//
//        // Assert
//        verify(terminalFrame).setVisible(true);
//        verify(screen).startScreen();
//        assertNotNull(uiService.getGui());
//    }
//
//    @Test
//    void handleKeyStroke_ShouldSendInputToGameBasedOnKeyStroke() {
//        // Arrange
//        UIService uiService = new UIService();
//        uiService.setGame(game);
//        KeyStroke keyStroke = new KeyStroke(KeyType.ArrowUp, ' ');
//
//        // Act
//        uiService.handleKeyStroke(keyStroke);
//
//        // Assert
//        verify(game).sendInput(any(), eq("up"));
//    }
//
//    @Test
//    void getHomeWindow_ShouldReturnWindowWithExpectedComponents() {
//        // Arrange
//        UIService uiService = new UIService();
//        // Mock dependencies as needed
//
//        // Act
//        BasicWindow homeWindow = uiService.getHomeWindow();
//
//        // Assert
//        assertNotNull(homeWindow);
//        // Write assertions to verify the contents of the home window
//    }
//
//    @Test
//    void getHostGameWindow_ShouldReturnWindowWithExpectedComponents() {
//        // Arrange
//        UIService uiService = new UIService();
//        // Mock dependencies as needed
//
//        // Act
//        BasicWindow hostGameWindow = uiService.getHostGameWindow();
//
//        // Assert
//        assertNotNull(hostGameWindow);
//        // Write assertions to verify the contents of the host game window
//    }
//
//    @Test
//    void getConfigureAgentWindow_ShouldReturnWindowWithExpectedComponents() {
//        // Arrange
//        UIService uiService = new UIService();
//        // Mock dependencies as needed
//
//        // Act
//        BasicWindow configureAgentWindow = uiService.getConfigureAgentWindow();
//
//        // Assert
//        assertNotNull(configureAgentWindow);
//        // Write assertions to verify the contents of the configure agent window
//    }
//
//    @Test
//    void updateGameWindow_ShouldUpdateGameWindowComponents() {
//        // Arrange
//        UIService uiService = new UIService();
//        // Mock dependencies as needed
//
//        // Act
//        uiService.updateGameWindow();
//
//        // Assert
//        // Write assertions to verify the updated game window
//    }
//
//    @Test
//    void renderWorld_ShouldRenderWorldToPanel() {
//        // Arrange
//        UIService uiService = new UIService();
//        // Mock dependencies as needed
//
//        World world = mock(World.class);
//        when(uiService.getWorld()).thenReturn(world);
//
//        // Act
//        Panel worldPanel = uiService.renderWorld();
//
//        // Assert
//        assertNotNull(worldPanel);
//        // Write assertions to verify the rendered world
//    }
//
//    @Test
//    void testErrorHandling() {
//        // Test error handling scenarios such as IOExceptions
//    }
//}
