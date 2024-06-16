package com.han.towersofivory.network.businesslayer.server;

import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;

import java.net.InetSocketAddress;

/**
 * IServer is an interface that extends the IOperation interface.
 * It provides the basic functionality for a server, such as starting, stopping, and getting server details.
 * It also provides a method to check if the server is connected.
 */
public interface IServer extends IOperation {
    /**
     * Starts the server with the given host address.
     *
     * @param hostAddress The address of the host.
     * @throws ConnectionFailedException If the server fails to start.
     */
    void start(InetSocketAddress hostAddress) throws ConnectionFailedException;

    /**
     * Stops the server.
     */
    void stop();

    /**
     * Retrieves the IP of the server.
     *
     * @return The IP of the server.
     */
    String getIp();

    /**
     * Retrieves the port of the server.
     *
     * @return The port of the server.
     */
    int getPort();

    /**
     * Checks if the server is connected.
     *
     * @return True if the server is connected, false otherwise.
     */
    boolean isConnected();
}