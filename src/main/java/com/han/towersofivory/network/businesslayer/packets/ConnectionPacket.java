package com.han.towersofivory.network.businesslayer.packets;

import com.han.towersofivory.network.businesslayer.packet.BasePacket;

import java.util.UUID;

/**
 * ConnectionPacket is a class that extends the BasePacket class.
 * It represents a packet of data related to a connection in the network layer.
 * It contains information about the source of the connection.
 */
public class ConnectionPacket extends BasePacket {
    private UUID connectionUuid;
    private final String sourceAddress;

    /**
     * Constructor for the ConnectionPacket class.
     *
     * @param sourceAddress The address of the source of the connection.
     */
    public ConnectionPacket(UUID connectionUuid, String sourceAddress) {
        this.connectionUuid = connectionUuid;
        this.sourceAddress = sourceAddress;
    }

    /**
     * Retrieves the UUID of the connection.
     *
     * @return The UUID of the connection.
     */
    public UUID getConnectionUuid() {
        return connectionUuid;
    }

    /**
     * Retrieves the source address of the connection.
     *
     * @return The source address of the connection.
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setConnectionUuid(UUID connectionUUID) {
        this.connectionUuid = connectionUUID;
    }
}