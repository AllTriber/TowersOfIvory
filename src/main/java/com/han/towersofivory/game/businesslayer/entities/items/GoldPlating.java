package com.han.towersofivory.game.businesslayer.entities.items;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;

/**
 * Represents a gold plating item.
 */
public class GoldPlating extends Item {

    public GoldPlating() {
        this.character = ASCIIInterfaceCharacters.GOLD_PLATING.getCharacter();
    }

    @Override
    public void addItem(Player player) {
        player.setHp(player.getHp() + 1);
    }

}
