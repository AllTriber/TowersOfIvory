package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UC 2 Spelen Spel -> US 2.4 Chatfunctie
 *
 * Represents the chat of the gameInstance.
 * Each message in messages is a message sent in the chat.
 */
public class Chat implements Serializable {

    private List<Message> messages;

    public Chat() {
        messages = new ArrayList<>();
    }

    /**
     * UC 2 Spelen Spel -> US 2.4 Chat
     *
     * Gives the entire message history of the player in the game.
     *
     * @param player - the UUID of the player who wants to see his message history.
     * @return a list containing the messages a player may read.
     */
    public List<Message> getAllMessagesForPlayer(UUID player) {
        List<Message> list = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getText(player) != null) {
                list.add(msg);
            }
        }
        return list;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

}
