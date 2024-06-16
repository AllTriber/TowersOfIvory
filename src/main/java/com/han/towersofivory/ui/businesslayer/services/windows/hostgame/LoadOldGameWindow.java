package com.han.towersofivory.ui.businesslayer.services.windows.hostgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.gamesave.GameSaver;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class LoadOldGameWindow extends AbstractWindow {
    private final GameMapper game;
    private final WindowRouter router;
    private final IAgent agent = new ConcreteAgent();
    public static final Logger LOGGER = LogManager.getLogger(LoadOldGameWindow.class);

    public LoadOldGameWindow(WindowRouter router, GameMapper game) {
        super("Host Game");
        this.router = router;
        this.game = game;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);

        ComboBox<String> agentConfig = renderAgentConfigurationComboBox();

        contentPanel.addComponent(new EmptySpace());

        renderSavedGames(agentConfig, contentPanel);

        contentPanel.addComponent(new EmptySpace());

        renderBackButton();
    }

    /**
     * UC9: Hervatten spel <br>
     * This method displays all the configured agents that are located in the src/main/resources/agentConfigurations folder.
     *
     * @return void
     */
    private ComboBox<String> renderAgentConfigurationComboBox() {
        // Load all configurations
        File[] configFiles = agent.loadAllConfigurations();

        // Convert file names to a list of strings
        List<String> configNames = new ArrayList<>();
        for (File configFile : configFiles) {
            configNames.add(configFile.getName());
        }

        contentPanel.addComponent(new Label("Select Agent Configuration:"));

        // Create ComboBox with the config names
        ComboBox<String> agentConfig = new ComboBox<>(configNames);
        contentPanel.addComponent(agentConfig);

        return agentConfig;
    }

    /**
     * UC9: Hervatten spel <br>
     * This method displays all the saved game states that are located in the src/main/resources/saveGames folder.
     *
     * @return void
     */
    private void renderSavedGames(ComboBox<String> agentConfig, Panel contentPanel) {
        final List<File> savedGameStates = new ArrayList<>();
        final int[] currentPage = {0};

        Panel savedGamesPanel = new Panel();

        for (File file : GameSaver.getAllSavedGames()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                savedGameStates.add(file);
            }
        }

        Runnable renderPage = getRunnable(agentConfig, savedGamesPanel, currentPage, 7, savedGameStates);

        contentPanel.addComponent(savedGamesPanel);

        renderPage.run();
    }

    /**
     * UC9: Hervatten spel <br>
     * This method returns a runnable that updates the saved games panel with the saved game states.
     *
     * @return Runnable
     */
    private Runnable getRunnable(ComboBox<String> agentConfig, Panel savedGamesPanel, int[] currentPage, int pageSize, List<File> savedGameStates) {
        return () -> updateSavedGamesPanel(agentConfig, savedGamesPanel, currentPage, pageSize, savedGameStates);
    }

    /**
     * UC9: Hervatten spel <br>
     * This method updates the saved games panel with the saved game states.
     *
     * @return void
     */
    private void updateSavedGamesPanel(ComboBox<String> agentConfig, Panel savedGamesPanel, int[] currentPage, int pageSize, List<File> savedGameStates) {
        savedGamesPanel.removeAllComponents();

        savedGamesPanel.addComponent(new Label("Saved games:"));

        int start = currentPage[0] * pageSize;
        int end = Math.min(start + pageSize, savedGameStates.size());

        for (int i = start; i < end; i++) {
            File file = savedGameStates.get(i);
            String gameStateName = file.getName().replace(".txt", "");
            savedGamesPanel.addComponent(new Button(gameStateName, () -> resumeOldGame(gameStateName, agentConfig.getSelectedItem())));
        }

        savedGamesPanel.addComponent(new EmptySpace());
        if (currentPage[0] > 0) {
            savedGamesPanel.addComponent(new Button("Previous", () -> {
                currentPage[0]--;
                updateSavedGamesPanel(agentConfig, savedGamesPanel, currentPage, pageSize, savedGameStates);
            }));
        }
        if (end < savedGameStates.size()) {
            savedGamesPanel.addComponent(new Button("Next", () -> {
                currentPage[0]++;
                updateSavedGamesPanel(agentConfig, savedGamesPanel, currentPage, pageSize, savedGameStates);
            }));
        }
    }


    /**
     * UC9: Hervatten spel <br>
     * This method starts a game with the selected game state.
     *
     * @return void
     */
    private void resumeOldGame(String gameStateName, String agentName) {
        try {
            GameState oldGame = GameSaver.loadGameState(gameStateName);
            game.setGameState(oldGame);

            GameConfiguration gameConfiguration = oldGame.getGameInfo().getGameConfiguration();
            game.configureGame(gameConfiguration);
            game.generateWorld(gameConfiguration);

            AgentConfigurationText agentConfiguration = agent.getAgent(agentName);
            game.getWorld().setMyAgentConfiguration(agentConfiguration);

            game.joinGame(game.createGame(oldGame.getGameInfo()), agentConfiguration);

            router.navigateTo("gameWindowWithoutWorld");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Error loading game state: {}", e.getMessage());
            showDialog();
        } catch (ConnectionFailedException e) {
            MessageDialog.showMessageDialog(router.gui(), "Error", "Connection failed: " + e.getMessage(), MessageDialogButton.OK);
            router.navigateTo("homeWindow");
        }
    }

    /**
     * UC9: Hervatten spel <br>
     * This method displays the back button that navigates back to the CreateOrLoadWindow window.
     *
     * @return void
     */
    private void renderBackButton(){
        contentPanel.addComponent(new Button("Back", () -> {
            close();
            router.navigateTo("createOrLoadWindow");
        }));
    }

    private void showDialog() {
        router.dialog("showErrorWindow", "Error", "An error occurred while loading the game state.");
    }
}
