package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public interface IKeyStrokeHandler {
    void startKeyListenerThread();
    void setTerminalFrame(SwingTerminalFrame terminalFrame);
}
