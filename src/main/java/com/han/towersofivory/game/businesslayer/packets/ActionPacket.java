package com.han.towersofivory.game.businesslayer.packets;

import com.han.towersofivory.network.businesslayer.packet.BasePacket;

import java.util.UUID;

public class ActionPacket extends BasePacket {
    private final UUID playerUUID;
    private final String action;
    private final String[] args;

    public ActionPacket(UUID playerUUID, String action, String[] args) {
        this.playerUUID = playerUUID;
        this.action = action;
        this.args = args;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getAction() {
        return action;
    }

    public String[] getArgs() {
        return args;
    }
}
