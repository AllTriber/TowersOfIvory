package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.TextBox;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import java.util.regex.Pattern;

/**
 * The chat window
 * Should only be used as a dialog
 */
public class ChatWindow extends AbstractWindow {
    private final WindowRouter router;
    private final Runnable onClose;
    private final GameMapper game;
    private final String inGameName;

    public ChatWindow(WindowRouter router, GameMapper game, Runnable onClose, String inGameName) {
        super("Proximity Chat");
        this.router = router;
        this.onClose = onClose;
        this.game = game;
        this.inGameName = inGameName;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        renderSendChatMessageTextBox();

        contentPanel.addComponent(new Button("Close", () -> {
            onClose.run();
            close();
        }));
    }

    private void renderSendChatMessageTextBox() {
        TextBox textBox = new TextBox();
        // allow only letters, numbers, and spaces and max 39 characters
        textBox.setValidationPattern(Pattern.compile("^[a-zA-Z0-9 ]{0,39}$"));
        contentPanel.addComponent(textBox);
        contentPanel.addComponent(new Button("Send", () -> {
            if (textBox.getText().isEmpty()) return;
            game.sendInput("chat [" + inGameName + "] " + textBox.getText());
            textBox.setText("");
        }));
    }

}
