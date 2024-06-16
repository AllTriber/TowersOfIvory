package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * UC 2 Spelen Spel -> US 2.4 Chatfunctie
 *
 * This class represents a message sent in the chat.
 */
public class Message implements Serializable {

    public Message(String text, List<UUID> players) {
        this.text = text;
        this.players = players;
    }

    private String text;

    private List<UUID> players;

    /**
     * UC 2 Spelen Spel -> US 2.4 Chatfunctie
     *
     * @param player - The UUID of the player trying to read the message.
     * @return - The message if the UUID is allowed to read. Else returns null
     */
    public String getText(UUID player) {
        for (UUID player2 : players) {
            if (player.equals(player2)){
                return text;
            }
        }
        return null;
    }

}
