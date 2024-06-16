package com.han.towersofivory.network.dto;

import com.han.towersofivory.game.businesslayer.GameInfo;

import java.net.InetSocketAddress;

public class HostInformation {
    private final InetSocketAddress address;
    private final GameInfo gameInfo;

    public HostInformation(InetSocketAddress address, GameInfo gameInfo) {
        this.address = address;
        this.gameInfo = gameInfo;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
