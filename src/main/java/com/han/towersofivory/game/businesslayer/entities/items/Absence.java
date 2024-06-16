package com.han.towersofivory.game.businesslayer.entities.items;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;

/**
 * Represents an absence item.
 */
public class Absence extends Item {
    public Absence() {
        this.character = ASCIIInterfaceCharacters.ABSENCE.getCharacter();
    }

    @Override
    public void addItem(Player player) {
        player.setDefense(player.getDefense() + 1);
    }

}
