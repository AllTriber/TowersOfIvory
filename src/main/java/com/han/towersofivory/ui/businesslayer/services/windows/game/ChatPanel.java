package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Chat;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Message;

import java.util.List;
import java.util.UUID;

/**
 * UC 2 Spelen Spel -> US 2.4 Chat
 *
 * The chat panel for the game window.
 */
public class ChatPanel extends Panel {

    public static final int MAX_CHAT_MESSAGES = 20;
    public static final String CHAT_PADDING = "                                             ";

    public ChatPanel() {
        super();
        this.addComponent(new Label(CHAT_PADDING));
    }

    /**
     * Updates the chat box with the latest messages.
     *
     * @param chat       - The chat object containing all messages.
     * @param playerUUID - The UUID of the player.
     */
    public void updateChatBox(Chat chat, UUID playerUUID) {
        this.removeAllComponents();

        this.addComponent(new Label("Chat:"));
        this.addComponent(new Label(CHAT_PADDING));


        this.addComponent(renderChatBox(chat.getAllMessagesForPlayer(playerUUID), playerUUID));
    }

    private Label renderChatBox(List<Message> messages, UUID playerUUID) {
        Label chatBox = new Label("");
        StringBuilder chatText = new StringBuilder();

        for (int i = Math.max(0, messages.size() - MAX_CHAT_MESSAGES); i < messages.size(); i++) {
            Message message = messages.get(i);
            chatText.append(message.getText(playerUUID)).append("\n");
        }

        chatBox.setText(chatText.toString());
        return chatBox;
    }


}