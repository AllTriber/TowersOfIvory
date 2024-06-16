package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Is an action the player enters into the frontend.
 */
public abstract class ActionBase {
    private final ArrayList<String> aliases;
    private final String input;
    private String usage = "";
    private String explain = "";

    protected ActionBase(String input) {
        this.aliases = new ArrayList<>();
        this.input = input;
    }

    protected ActionBase(String input, String usage) {
        this(input);

        this.usage = usage;
    }

    protected ActionBase(String input, String usage, String explain) {
        this(input);

        this.usage = usage;
        this.explain = explain;
    }

    public void addAliasses(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public boolean hasAlias(String alias) {
        return aliases.contains(alias);
    }

    /*
     * UC 2 Spelen spel
     *
     * In this method the player performs an action, and changes the gamestate, e.g. attacking or moving.
     * This method is invoked by the host, who has already checked in the checkAction wether this action can be performed.
     *
     * @:Param: player, the player who performed the action
     * @:Param: world, the current gamestate to be changed
     * @:Param: cmd, the command the player has entered into the TUI, e.g. up or down
     * @:Param: args, the optional arguments to the command the player entered into the TUI.
     */
    public abstract void performAction(Player player, World world, String cmd, String[] args) throws IOException;

    /*
     * UC 2 Spelen Spel
     *
     * Checks an action which changes the gamestate a player ought to performs.
     * This method is done on the Host evironment.
     *
     * @:Param: player, the player who performed the action
     * @:Param: world, the current gamestate to be changed
     * @:Param: cmd, the command the player has entered into the TUI, e.g. up or down
     * @:Param: args, the optional arguments to the command the player entered into the TUI.
     */
    public boolean checkAction(Player player, World world, String cmd, String[] args){
        if(player.getHp() > 0){
            return specificCheckAction(player, world, cmd, args);
        }
        return false;
    }

    public abstract boolean specificCheckAction(Player player, World world, String cmd, String[] args);

    public String getInput() {
        return input;
    }

    public String getUsage() {
        return usage;
    }

    public String getExplain() {
        return explain;
    }
}
