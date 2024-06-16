package com.han.towersofivory.ui.businesslayer.services;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.han.towersofivory.ui.businesslayer.services.windows.WindowConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WindowRouterTest {

    private WindowBasedTextGUI gui;
    private WindowConfiguration windowConfiguration;
    private SwingTerminalFrame terminalFrame;
    private WindowRouter windowRouter;

    @BeforeEach
    void setUp() {
        gui = mock(WindowBasedTextGUI.class);
        windowConfiguration = mock(WindowConfiguration.class);
        terminalFrame = mock(SwingTerminalFrame.class);
        windowRouter = new WindowRouter(gui, windowConfiguration, terminalFrame);
    }

    @Test
    void testNavigateTo() {
        String windowClass = "qwertyuiop";
        Object[] params = {"param1", "param2"};
        Object[] expectedParams = {windowRouter, "param1", "param2"};

        when(gui.getActiveWindow()).thenReturn(null);
        when(windowConfiguration.createWindow(windowClass, expectedParams)).thenReturn(null);

        windowRouter.navigateTo(windowClass, params);

        verify(gui).removeWindow(null);
        verify(gui).addWindowAndWait(null);
        verify(windowConfiguration).createWindow(windowClass, expectedParams);
    }

    @Test
    void testDialog() {
        String windowClass = "qwertyuiop";
        Object[] params = {"param1", "param2"};
        Object[] expectedParams = {windowRouter, "param1", "param2"};

        when(windowConfiguration.createWindow(windowClass, expectedParams)).thenReturn(null);

        windowRouter.dialog(windowClass, params);

        verify(gui).addWindowAndWait(null);
        verify(windowConfiguration).createWindow(windowClass, expectedParams);
    }

    @Test
    void testGetTerminalFrame() {
        assertEquals(terminalFrame, windowRouter.getTerminalFrame());
    }
}
