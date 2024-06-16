package com.han.towersofivory.ui.interfacelayer.interfaces;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

/**
 * Interface for updating the UI.
 *
 * @author Pepijn van den Ende
 * @version 1.0
 * @since 1.0
 */
public interface IUpdateUI {
    void updateUI(World world, BasePacket packet); //Was String action
}
