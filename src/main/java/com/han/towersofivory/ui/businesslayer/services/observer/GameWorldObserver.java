package com.han.towersofivory.ui.businesslayer.services.observer;

import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

public interface GameWorldObserver {
    void updateUI(World world, BasePacket packet);
}
