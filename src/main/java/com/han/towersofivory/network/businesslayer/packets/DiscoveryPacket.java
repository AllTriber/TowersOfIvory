package com.han.towersofivory.network.businesslayer.packets;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

/**
 * DiscoveryPacket is a class that extends the BasePacket class.
 * It represents a packet of data related to a discovery in the network layer.
 * It contains information about the source of the discovery.
 */
public class DiscoveryPacket extends BasePacket {
    private final String sourceAddress;
    private final int sourcePort;
    private GameInfo gameInfo;

    /**
     * Constructor for the DiscoveryPacket class.
     *
     * @param sourceAddress The address of the source of the discovery.
     * @param sourcePort    The port of the source of the discovery.
     */
    public DiscoveryPacket(String sourceAddress, int sourcePort) {
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
    }

    public DiscoveryPacket(String sourceAddress, int sourcePort, GameInfo gameInfo) {
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.gameInfo = gameInfo;
    }

    /**
     * Retrieves the source address of the discovery.
     *
     * @return The source address of the discovery.
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Retrieves the source port of the discovery.
     *
     * @return The source port of the discovery.
     */
    public int getSourcePort() {
        return sourcePort;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}