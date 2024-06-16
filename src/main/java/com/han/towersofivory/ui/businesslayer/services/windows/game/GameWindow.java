package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.gamesave.PlayerNameHandler;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.ui.businesslayer.services.ASCIIWorldService;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.observer.GameWorldObserver;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The game window
 */
public class GameWindow extends AbstractWindow implements GameWorldObserver {
    private static final Logger LOGGER = LogManager.getLogger(GameWindow.class);

    private final GameMapper game;
    private final Panel gamePanel;
    private PlayerStatsPanel playerStatsPanel;
    private ChatPanel chatPanel;
    private final SwingTerminalFrame terminalFrame;
    private final ASCIIWorldService worldService;
    private final WindowRouter router;
    private final String inGameName;
    private DefaultKeyStrokeHandler keyStrokeHandler;
    private World world;

    public GameWindow(WindowRouter router, GameMapper game, ASCIIWorldService worldService, World world) {
        super("Game Window");
        this.game = game;
        this.router = router;
        this.world = world;
        this.gamePanel = new Panel();
        this.terminalFrame = router.getTerminalFrame();
        this.worldService = worldService;
        PlayerNameHandler playerNameHandler = new PlayerNameHandler();
        this.inGameName = playerNameHandler.readPlayerNameFromFile();
        initializeComponents();
    }

    public GameWindow(WindowRouter router, GameMapper game, ASCIIWorldService worldService) {
        this(router, game, worldService, null);
    }

    @Override
    protected void initializeComponents() {
        contentPanel.setLayoutManager(new BorderLayout());

        playerStatsPanel = new PlayerStatsPanel();
        playerStatsPanel.setLayoutManager(new GridLayout(1));

        chatPanel = new ChatPanel();
        chatPanel.setLayoutManager(new GridLayout(1));

        contentPanel.addComponent(gamePanel, BorderLayout.Location.CENTER);
        contentPanel.addComponent(playerStatsPanel, BorderLayout.Location.RIGHT);
        contentPanel.addComponent(chatPanel, BorderLayout.Location.LEFT);
        setGameWindowComponent();
        if (world != null) {
            updateGameWindow(world);
        }

        this.setHints(List.of(Hint.FULL_SCREEN));
    }

    private void setGameWindowComponent() {
        keyStrokeHandler = new DefaultKeyStrokeHandler(game, this);
        keyStrokeHandler.setTerminalFrame(terminalFrame);
        keyStrokeHandler.startKeyListenerThread();
    }

    @Override
    public void updateUI(World world, BasePacket packet) {
        LOGGER.info("Updating UI");
        this.world = world;
        updateGameWindow(world);
        playerStatsPanel.updatePlayerStats(world.getMyPlayer().getStats());
        chatPanel.updateChatBox(world.getChat(), world.getMyPlayer().getUUID());
    }

    private void updateGameWindow(World world) {
        clearAndSetupGamePanel();
        Player player = world.getMyPlayer();
        String windowId = "gameOverWindow";

        if (player != null) {
            if (player.getHp() > 0) {
                Panel worldPanel = renderWorld(world);
                worldPanel.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true));
                gamePanel.addComponent(worldPanel, BorderLayout.Location.CENTER);
            } else {
                keyStrokeHandler.stopKeyListener();
                if (player.getPosition().getZ() <= world.getHighestFloorLevel() - world.getMaxFloors()) {
                    getTextGUI().getGUIThread().invokeLater(() -> router.dialog(windowId, "The floor collapsed. You died.", "Game over"));
                } else {
                    getTextGUI().getGUIThread().invokeLater(() -> router.dialog(windowId, "You died.", "Game over"));
                }
            }
        }
        /*
         * checks if the gameStopped in world has been set to true so every player has to stop handling keystrokes
         * also gives every player the gameOverWindow where the player can close the game
         */
        if (world.gameStopped()) {
            keyStrokeHandler.stopKeyListener();
            getTextGUI().getGUIThread().invokeLater(() -> router.navigateTo("gameOverWindow", "The host has stopped the game ):", "Melding"));
        }
    }

    private void clearAndSetupGamePanel() {
        this.gamePanel.removeAllComponents();
        this.gamePanel.setLayoutManager(new GridLayout(3));
    }

    private Panel renderWorld(World world) {
        Panel worldPanel = new Panel();

        worldPanel.addComponent(new Label(worldService.render(world)));

        return worldPanel;
    }

    public void gotoChat() {
        Runnable onClose = this::setGameWindowComponent;
        getTextGUI().getGUIThread().invokeLater(() -> router.dialog("ChatWindow", onClose, inGameName));
    }

    public void pauseScreen() {
        keyStrokeHandler.stopKeyListener();
        Runnable onClose = () -> keyStrokeHandler.startKeyListenerThread();
        getTextGUI().getGUIThread().invokeLater(() -> router.dialog("leaveGameWindow",  onClose));
    }
}
