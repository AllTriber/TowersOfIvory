package com.han.towersofivory.ui.businesslayer.services.windows.home;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.TextBox;
import com.han.towersofivory.game.businesslayer.gamesave.PlayerNameHandler;
import com.han.towersofivory.game.businesslayer.gamesave.PlayerUUIDHandler;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import java.util.regex.Pattern;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class HomeWindow extends AbstractWindow {
    private final WindowRouter router;
    private final PlayerNameHandler playerNameHandler;

    public HomeWindow(WindowRouter router) {
        super("Home");
        this.router = router;
        this.playerNameHandler = new PlayerNameHandler();
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);
        contentPanel.addComponent(new Label("Name:"));
        // add input field for own name
        TextBox nameInput = new TextBox();
        if (playerNameHandler.checkMyPlayerNameExists()) {
            nameInput.setText(playerNameHandler.readPlayerNameFromFile());
        }

        // only allow alphanumeric characters and max length of 12
        nameInput.setValidationPattern(Pattern.compile("^[a-zA-Z0-9]{1,12}$"));
        contentPanel.addComponent(nameInput);

        contentPanel.addComponent(new Button("Join Game", () -> {
            playerNameHandler.writePlayerNameToFile(nameInput.getText());
            router.navigateTo("loadHostsWindow", 1000L);
        }));
        contentPanel.addComponent(new Button("Host Game", () -> {
            playerNameHandler.writePlayerNameToFile(nameInput.getText());
            router.navigateTo("createOrLoadWindow");
        }));
        contentPanel.addComponent(new Button("Configure Agent", () -> {
            playerNameHandler.writePlayerNameToFile(nameInput.getText());
            router.navigateTo("agentWindow");
        }));
        contentPanel.addComponent(new Button("Exit Game", () -> {
            playerNameHandler.writePlayerNameToFile(nameInput.getText());
            System.exit(0);
        }));
    }
}
