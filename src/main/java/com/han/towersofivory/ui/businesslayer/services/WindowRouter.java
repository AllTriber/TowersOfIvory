package com.han.towersofivory.ui.businesslayer.services;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.han.towersofivory.ui.businesslayer.services.windows.WindowConfiguration;

/**
 * This class is responsible for creating and displaying windows based on their ID and parameters.
 */
public record WindowRouter(WindowBasedTextGUI gui, WindowConfiguration windowConfiguration, SwingTerminalFrame terminalFrame) {

    /**
     * Navigate to a window with the given ID and parameters.
     * The window ID must be registered in {@link WindowConfiguration}.
     * The number of parameters must match the expected number of parameters for the window.
     * The parameters must match the expected types for the window.
     * The first parameter of the window must be a {@link WindowRouter}.
     * <p>
     * @param windowId The ID of the window to navigate to.
     * @param params   The parameters to pass to the window.
     * @throws IllegalArgumentException If the window ID is unknown, or if the number of parameters does not match the
     *                                  expected number of parameters for the window.
     */
    public void navigateTo(String windowId, Object... params) {
        params = addRouterTo(params);
        gui.removeWindow(gui().getActiveWindow());
        gui.addWindowAndWait(windowConfiguration.createWindow(windowId, params));
    }

    /**
     * Display a dialog window with the given ID and parameters.
     * The window ID must be registered in {@link WindowConfiguration}.
     * The number of parameters must match the expected number of parameters for the window.
     * The parameters must match the expected types for the window.
     * The first parameter of the window must be a {@link WindowRouter}.
     * <p>
     * @param windowId The ID of the window to display.
     * @param params   The parameters to pass to the window.
     * @throws IllegalArgumentException If the window ID is unknown, or if the number of parameters does not match the
     *                                  expected number of parameters for the window.
     */
    public void dialog(String windowId, Object... params) {
        params = addRouterTo(params);
        gui.addWindowAndWait(windowConfiguration.createWindow(windowId, params));
    }

    /**
     * Add this router as the first parameter in the given array of parameters.
     *
     * @param params The parameters to add this router to.
     * @return The parameters with this router added as the first parameter.
     */
    private Object[] addRouterTo(Object[] params) {
        Object[] paramsWithRouter = new Object[params.length + 1];
        paramsWithRouter[0] = this;
        System.arraycopy(params, 0, paramsWithRouter, 1, params.length);
        params = paramsWithRouter;
        return params;
    }

    public SwingTerminalFrame getTerminalFrame() {
        return terminalFrame;
    }
}

