package com.han.towersofivory.network.businesslayer.packet;

/**
 * PacketSource is an enumeration that defines the source of packets.
 * It is used to identify the origin of packets in the network layer.
 * The sources are defined as follows:
 * SERVER: The packet originated from the server.
 * CLIENT: The packet originated from the client.
 */
public enum PacketSource {
    SERVER(0),
    CLIENT(1);

    private final int source;

    /**
     * Constructor for the PacketSource enumeration.
     *
     * @param source The source as an integer.
     */
    PacketSource(int source) {
        this.source = source;
    }

    /**
     * Retrieves the source as an integer.
     *
     * @return The source as an integer.
     */
    public int getSource() {
        return source;
    }
}