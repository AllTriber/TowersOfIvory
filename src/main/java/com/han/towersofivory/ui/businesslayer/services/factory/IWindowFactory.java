package com.han.towersofivory.ui.businesslayer.services.factory;

import com.googlecode.lanterna.gui2.Window;

/**
 * Interface for a window factory.
 */
public interface IWindowFactory {

    /**
     * Create a window with the given ID and parameters.
     *
     * @param windowId The ID of the window to create.
     * @param args parameters to pass to the window.
     * @return The created window.
     */
    Window createWindow(String windowId, Object... args);
}