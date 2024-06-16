package com.han.towersofivory.game.businesslayer.entities.items;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.ASCIIInterfaceCharacters;

/**
 * Represents a chest item.
 */
public class Chest extends Item {
    private final int effect;

    public Chest(int effect) {
        this.effect = effect;
        this.character = ASCIIInterfaceCharacters.CHEST.getCharacter();
    }

    @Override
    public void addItem(Player player) {
        switch (effect) {
            case 0:
                player.setAttack(player.getAttack() + 1);
                break;
            case 1:
                player.setHp(player.getHp() + 1);
                break;
            case 2:
                player.setDefense(player.getDefense() + 1);
                break;
            default:
                break;
        }
    }
}