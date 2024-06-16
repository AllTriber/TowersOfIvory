package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionDispatcher {
    private final List<ActionBase> actions = new ArrayList<>();

    // API
    public void register(ActionBase base) {
        actions.addFirst(base);
    }

    public void unregister(ActionBase base) {
        actions.remove(base);
    }

    public ActionBase getByAction(String cmd) {
        for (ActionBase action : actions) {
            if (action.getInput().equalsIgnoreCase(cmd) || action.hasAlias(cmd)) {
                return action;
            }
        }
        return null;
    }

    public List<ActionBase> getActions() {
        return actions;
    }

    public void onAction(Player player, World world, String input, String[] args) throws IOException {
        ActionBase action = getByAction(input);
        if (action != null) {
            action.performAction(player, world, input, args);
        }
    }

    public boolean onCheck(Player player, World world, String input, String[] args) {
        ActionBase action = getByAction(input);
        if (action != null) {
            return action.checkAction(player, world, input, args);
        }

        return false;
    }
}