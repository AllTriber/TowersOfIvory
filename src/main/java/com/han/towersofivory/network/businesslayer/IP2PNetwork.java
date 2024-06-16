package com.han.towersofivory.network.businesslayer;

import java.net.InetSocketAddress;

/**
 * IP2PNetwork is an interface for classes that represent a peer-to-peer network.
 * Classes implementing this interface can provide access to a host, a client, and the server address.
 */
public interface IP2PNetwork {
    /**
     * Retrieves the host of the peer-to-peer network.
     *
     * @return The host of the peer-to-peer network.
     */
    P2PHost getHost();

    /**
     * Retrieves the client of the peer-to-peer network.
     *
     * @return The client of the peer-to-peer network.
     */
    P2PClient getClient();

    /**
     * Retrieves the server address of the peer-to-peer network.
     *
     * @return The server address of the peer-to-peer network.
     */
    InetSocketAddress getServerAddress();
}