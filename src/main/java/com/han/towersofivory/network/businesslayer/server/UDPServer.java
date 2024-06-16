package com.han.towersofivory.network.businesslayer.server;

import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;

import java.net.InetSocketAddress;

/**
 * UDPServer is a class that extends the BaseUDPServer class.
 * It represents a UDP server that can start with a given host address.
 */
public class UDPServer extends BaseUDPServer {
    /**
     * Starts the server with the given host address.
     *
     * @param hostAddress The address of the host.
     * @throws ConnectionFailedException If the server fails to start.
     */
    @Override
    public void start(InetSocketAddress hostAddress) throws ConnectionFailedException {
        internalStart(hostAddress);
    }
}