package com.han.towersofivory.game.businesslayer.entities.items;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;

/**
 * Represents a C4 model item.
 */
public class C4Model extends Item {
    public C4Model() {
        this.character = ASCIIInterfaceCharacters.C4_MODEL.getCharacter();
    }

    @Override
    public void addItem(Player player) {
        player.setAttack(player.getAttack() + 1);
    }
}
