package com.han.towersofivory.game.businesslayer.action;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;

public class EliminationActionExecutor extends ActionBase{

    public EliminationActionExecutor() {
        super("eliminate", null, "Eliminates the player");
    }

    @Override
    public void performAction(Player player, World world, String cmd, String[] args) {
        player.setPosition(new Position(0, 0, 0));
        player.setHp(0);
    }

    @Override
    public boolean specificCheckAction(Player player, World world, String cmd, String[] args) {
        return true;
    }
}
