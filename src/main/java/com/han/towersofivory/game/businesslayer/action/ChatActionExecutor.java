package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.Message;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UC 2 -> US 2.4 Chat
 *
 * This class handles the input from the chat window and saves chat messages to the gamestate.
 */
public class ChatActionExecutor extends ActionBase {
    private static final Logger LOGGER = LogManager.getLogger(ChatActionExecutor.class);

    public ChatActionExecutor() {
        super("chat", "chat [message]", "Send a message to all players in the area.");
    }

    // Note that the Message.text is entirely in the args where each arg is a word.
    @Override
    public void performAction(Player player, World world, String cmd, String[] args) {
        LOGGER.info("Performing chat action");

        // Filter for capture the flag
        // Implemented later

        world.getChat().addMessage(buildChatMessage(player, args, world));
    }

    List<UUID> findPlayersInProximity(Player player, World world) {
        return new ArrayList<>(world.getPlayersInProximity(player));
    }

    String buildChatString(String[] args) {
        return String.join(" ", args);
    }

    Message buildChatMessage(Player player, String[] args, World world) {
        return new Message(buildChatString(args), findPlayersInProximity(player, world));
    }

    @Override
    public boolean specificCheckAction(Player player, World world, String cmd, String[] args) {
        // There's nothing to check when sending a chat to other players.
        return true;
    }
}
