package com.han.towersofivory.game.businesslayer.packets;

import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

public class MovementPacket extends BasePacket {

    private final Player entity;

    public MovementPacket(Player entity) {
        this.entity = entity;
    }

    public Player getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "MovementPacket{" +
                "entity=" + entity +
                '}';
    }
}