package com.han.towersofivory.ui.businesslayer.services.windows.leavegame;

import com.googlecode.lanterna.gui2.Button;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A window that asks the user if they want to leave the game.
 * If the user is the host, they can also stop the game.
 * the user can also cancel the action and go back to the game
 */
public class LeaveGameWindow extends AbstractWindow {
    private static final Logger LOGGER = LogManager.getLogger(LeaveGameWindow.class);

    private final WindowRouter router;
    private boolean isHost = true;
    private final GameMapper gameMapper;
    private final Runnable onClose;

    public LeaveGameWindow(WindowRouter router, GameMapper gameMapper, Runnable onClose) {
        super("Leave Game?");
        this.router = router;
        this.gameMapper = gameMapper;
        this.onClose = onClose;
        initializeComponents();
    }

    /**
     * Initialize the components of the window In this case a button to leave the game (using System.exit(0)), a button
     * to pause the game and a button to cancel the action (close the window and go back to the game)
     */

    @Override
    public void initializeComponents() {
        contentPanel.addComponent(new Button("Leave game", this::leaveGame));

        // isHost is hardcoded true for now, could be improved later to only show button to host.
        if (isHost) {
            contentPanel.addComponent(new Button("Stop game (host only)", this::stopGame));
        }
        contentPanel.addComponent(new Button("Back to game", () -> {
            onClose.run();
            this.close();
        }));
    }

    /**
     * Leave the game by closing the window and navigating to the home window
     */
    private void leaveGame() {

        try {
            LOGGER.info("Saving game state");
            gameMapper.saveGameState();
            LOGGER.info("Leaving game");
            System.exit(0);
        }

        catch (IOException e) {
            LOGGER.error("Failed to save game state", e);
        }
    }

    /**
     * Stop the game by sending a message to the host, and then to other players.
     * only the host can stop the game
     */
    private void stopGame() {
        gameMapper.sendInput("stopGame");
    }
}