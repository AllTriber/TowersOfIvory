package com.han.towersofivory.ui.businesslayer.services.windows.joingame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * UC4 Deelnemen Spel
 * The purpose of this class is to show the join screen.
 */
public class JoinGameWindow extends AbstractWindow {
    private final WindowRouter router;
    private final GameMapper game;
    private final List<HostInformation> availableHosts;
    private final IAgent agent = new ConcreteAgent();

    public JoinGameWindow(WindowRouter router, GameMapper game, List<HostInformation> availableHosts) {
        super("Join Game");
        this.router = router;
        this.game = game;
        this.availableHosts = availableHosts;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        contentPanel.addComponent(new Label("Select Agent Configuration:"));

        // Load all configurations
        File[] configFiles = agent.loadAllConfigurations();

        // Convert file names to a list of strings
        List<String> configNames = new ArrayList<>();
        for (File configFile : configFiles) {
            configNames.add(configFile.getName());
        }

        // Create ComboBox with the config names
        ComboBox<String> agentConfig = new ComboBox<>(configNames);
        contentPanel.addComponent(agentConfig);

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label("Available Games:").setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));

        if (availableHosts.isEmpty()) {
            contentPanel.addComponent(new Label("No available hosts found.").setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
        } else {
            for (HostInformation hostInformation : availableHosts) {
                if (hostInformation.getGameInfo().getGameConfiguration() == null)
                    continue;

                contentPanel.addComponent(new Label("Game name: " + hostInformation.getGameInfo().getGameConfiguration().getName()).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
                contentPanel.addComponent(new Label("Players: " + hostInformation.getGameInfo().getNumberOfPlayers()).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
                contentPanel.addComponent(new Label("Gamemode: " + hostInformation.getGameInfo().getGameConfiguration().getGamemode()).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
                Button joinbutton = new Button("Join", () -> {
                    try {
                        game.generateWorld(hostInformation.getGameInfo().getGameConfiguration());
                        String selectedAgentConfig = agentConfig.getSelectedItem();
                        game.joinGame(hostInformation, agent.getAgent(selectedAgentConfig));
                        router.navigateTo("gameWindowWithoutWorld");
                    } catch (ConnectionFailedException e) {
                        MessageDialog.showMessageDialog(router.gui(), "Error", "Connection failed: " + e.getMessage(), MessageDialogButton.OK);
                    }
                });
                contentPanel.addComponent(joinbutton.setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(1)));
                contentPanel.nextFocus(joinbutton);
            }
        }

        contentPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        contentPanel.addComponent(new Button("Refresh", () -> showLoadingScreen(1000L)).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(1)));
        contentPanel.addComponent(new Button("Exit", () -> router.navigateTo("homeWindow")).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(1)));
    }

    private void showLoadingScreen(long l) {
        router.navigateTo("loadHostsWindow", l);
    }
}
