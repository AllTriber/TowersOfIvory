package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Handles keystrokes from the user in the game window
 */
@Service
public class DefaultKeyStrokeHandler implements IKeyStrokeHandler {

    private static final Logger LOGGER = LogManager.getLogger(DefaultKeyStrokeHandler.class);
    private final GameMapper game;
    private SwingTerminalFrame terminalFrame;
    private final GameWindow gameWindow;
    private Thread keyListenerThread;
    private long lastInputTime = 0;
    private static final long INPUT_DELAY = 50;

    public DefaultKeyStrokeHandler(GameMapper game, GameWindow gameWindow) {
        this.game = game;
        this.gameWindow = gameWindow;
    }

    /**
     * Starts a thread that listens for keystrokes
     */
    @Override
    public void startKeyListenerThread() {
        keyListenerThread = new Thread(() -> {
            while (!keyListenerThread.isInterrupted()) {
                try {
                    KeyStroke keyStroke = terminalFrame.readInput();
                    if (keyStroke != null) {
                        handleKeyStroke(keyStroke);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error reading input", e);
                    keyListenerThread.interrupt();
                }
            }
            LOGGER.info("Key listener thread stopped");
        });
        keyListenerThread.start();
    }

    private void handleKeyStroke(KeyStroke keyStroke) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInputTime < INPUT_DELAY) {
            return;
        }
        lastInputTime = currentTime;
        switch (keyStroke.getKeyType()) {
            case ArrowUp -> game.sendInput("interactup");
            case ArrowDown -> game.sendInput("interactdown");
            case ArrowLeft -> game.sendInput("interactleft");
            case ArrowRight -> game.sendInput("interactright");
            case Escape -> gameWindow.pauseScreen();
            case Character -> {
                char key = keyStroke.getCharacter();
                switch (Character.toLowerCase(key)) {
                    case 'w' -> game.sendInput("up");
                    case 's' -> game.sendInput("down");
                    case 'a' -> game.sendInput("left");
                    case 'd' -> game.sendInput("right");
                    case 't' -> {
                        stopKeyListener();
                        gameWindow.gotoChat();
                    }
                    default -> {
                        // Do nothing
                    }
                }
            }
            default -> {
                // Do nothing
            }
        }
    }

    public void stopKeyListener() {
        keyListenerThread.interrupt();
    }

    @Override
    public void setTerminalFrame(SwingTerminalFrame terminalFrame) {
        this.terminalFrame = terminalFrame;
    }
}
