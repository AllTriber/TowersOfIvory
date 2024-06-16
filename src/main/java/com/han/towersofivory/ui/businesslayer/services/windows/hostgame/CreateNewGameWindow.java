package com.han.towersofivory.ui.businesslayer.services.windows.hostgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.game.businesslayer.GameConfiguration;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.Gamemode;
import com.han.towersofivory.game.businesslayer.gamesave.GameSaver;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.GameState;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.agent.DialogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;


public class CreateNewGameWindow extends AbstractWindow {
    public static final Logger LOGGER = LogManager.getLogger(CreateNewGameWindow.class);
    private final WindowRouter router;
    private final IAgent agent = new ConcreteAgent();
    private final GameMapper game;
    private static final String ERROR = "Error";

    public CreateNewGameWindow(WindowRouter router, GameMapper game) {
        super("Host Game");
        this.router = router;
        this.game = game;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);
        // Name of the game
        TextBox gameName = renderNameTextBox();
        contentPanel.addComponent(new EmptySpace());
        // Game mode
        ComboBox<String> gameMode = renderGamemodeComboBox();
        contentPanel.addComponent(new EmptySpace());
        // Agent configuration selection
        ComboBox<String> agentConfig = renderAgentConfigurationComboBox();
        contentPanel.addComponent(new EmptySpace());
        // Seed for the game
        TextBox gameSeed = renderSeedTextBox();
        renderStartButton(gameName, gameMode, gameSeed, agentConfig);
        contentPanel.addComponent(new EmptySpace());
        renderExitButton();
    }

    private void renderExitButton() {
        contentPanel.addComponent(new Button("Back", () -> router.navigateTo("createOrLoadWindow")));
    }

    private void showDialog(String title, String message, DialogType type, Runnable onConfirm) {
        router.dialog("agentDialogWindow", title, message, type, onConfirm);
    }

    private boolean checkIfGameNameExists(String gameName) {
        return Arrays.stream(GameSaver.getAllSavedGames()).anyMatch(file -> file.getName().equals(gameName + ".txt"));
    }

    private void renderStartButton(TextBox gameName, ComboBox<String> gameMode, TextBox gameSeed, ComboBox<String> agentConfig) {
        contentPanel.addComponent(new Button("Start Game", () -> {

            String name = gameName.getText();

            if (gameName.getText().trim().isEmpty()) {
                showDialog(ERROR, "Er moet een naam ingevuld worden.", DialogType.ERROR, null);
            } else if (checkIfGameNameExists(name)) {
                showDialog("Waarschuwing", "Een spel met deze titel bestaat al. Wilt u deze overschrijven?", DialogType.WARNING, () -> startGame(gameSeed, gameMode, agentConfig, name));
            } else {
                startGame(gameSeed, gameMode, agentConfig, name);
            }
        }));
    }

    private void startGame(TextBox gameSeed, ComboBox<String> gameMode, ComboBox<String> agentConfig, String gameName) {
        Long seed = game.getSeed(gameSeed.getText());

        Gamemode gamemode = Gamemode.fromFullName(gameMode.getSelectedItem());
        GameConfiguration gameConfiguration = new GameConfiguration(gameName, gamemode, seed);
        GameInfo gameInfo = new GameInfo(gameConfiguration, 0);
        GameState gameState = new GameState(gameInfo);
        game.setGameState(gameState);

        try {
            GameSaver.saveGameState(gameState);
        } catch (IOException e) {
            LOGGER.info(e);
        }


        try {
            HostInformation host = game.createGame(gameInfo);
            game.configureGame(gameConfiguration);
            game.generateWorld(gameConfiguration);
            AgentConfigurationText newAgent = agent.getAgent(agentConfig.getSelectedItem());
            game.getWorld().setMyAgentConfiguration(newAgent);
            game.joinGame(host, newAgent);
            router.navigateTo("gameWindowWithoutWorld");
        } catch (ConnectionFailedException e) {
            MessageDialog.showMessageDialog(router.gui(), ERROR, "Connection failed: " + e.getMessage(), MessageDialogButton.OK);
            router.navigateTo("homeWindow");
        }
    }

    private TextBox renderNameTextBox() {
        contentPanel.addComponent(new Label("Name:"));
        TextBox gameName = new TextBox();
        gameName.setValidationPattern(Pattern.compile("[a-zA-Z0-9 ]{1,20}"));
        contentPanel.addComponent(gameName);
        return gameName;
    }

    private ComboBox<String> renderGamemodeComboBox() {
        contentPanel.addComponent(new Label("Gamemode:"));
        ComboBox<String> gameMode = new ComboBox<>(Arrays.stream(Gamemode.values())
                .map(Gamemode::getFullName)
                .toList());
        contentPanel.addComponent(gameMode);
        return gameMode;
    }

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

    private TextBox renderSeedTextBox() {
        contentPanel.addComponent(new Label("Seed: (Leave empty to generate random seed)"));
        TextBox gameSeed = new TextBox();
        gameSeed.setValidationPattern(Pattern.compile("\\d{0,9}"));
        contentPanel.addComponent(gameSeed);
        return gameSeed;
    }
}