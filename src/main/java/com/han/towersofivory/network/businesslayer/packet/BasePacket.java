package com.han.towersofivory.network.businesslayer.packet;

import java.util.UUID;

/**
 * BasePacket is an abstract class that represents a packet of data.
 * It contains a timestamp, a UUID, and a source (client or server).
 * This class provides methods to get the timestamp, UUID, and source of the packet,
 * check if the packet is from the server or client, and set the UUID and source of the packet.
 */
public abstract class BasePacket {
    private final long timestamp;
    private UUID uuid;
    private PacketSource source;

    /**
     * Constructor for the BasePacket class.
     * Initializes the timestamp to the current system time.
     */
    protected BasePacket() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the timestamp of the packet.
     *
     * @return The timestamp of the packet.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the UUID of the packet.
     *
     * @return The UUID of the packet.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of the packet.
     *
     * @param uuid The UUID to set.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Checks if the packet is from the server.
     *
     * @return True if the packet is from the server, false otherwise.
     */
    public boolean isFromServer() {
        return this.source == PacketSource.SERVER;
    }

    /**
     * Checks if the packet is from the client.
     *
     * @return True if the packet is from the client, false otherwise.
     */
    public boolean isFromClient() {
        return this.source == PacketSource.CLIENT;
    }

    /**
     * Sets the source of the packet.
     *
     * @param source The source to set.
     */
    public void setSource(PacketSource source) {
        this.source = source;
    }
}