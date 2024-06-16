package com.han.towersofivory.network.businesslayer.server;

import com.han.towersofivory.network.businesslayer.connection.UDPConnection;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;

/**
 * BaseUDPServer is an abstract class that implements the IServer interface.
 * It provides the basic functionality for a UDP server, such as starting, stopping, and sending packets.
 * It also provides methods for getting the server's IP and port, checking if it's connected, and executing the server's main loop.
 */
public abstract class BaseUDPServer implements IServer {
    protected DatagramChannel datagramChannel;
    private UDPConnection connection;

    /**
     * Starts the server with the given host address.
     *
     * @param hostAddress The address of the host.
     * @throws ConnectionFailedException If the server fails to start.
     */
    protected void internalStart(InetSocketAddress hostAddress) throws ConnectionFailedException {
        try {
            datagramChannel = createDatagramChannel();
            datagramChannel.bind(hostAddress);
            connection = createUDPConnection(datagramChannel);
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to start UDP server", e);
        }
    }

    /**
     * Creates a datagram channel with the required settings.
     *
     * @return The configured datagram channel.
     * @throws IOException If an I/O error occurs.
     */
    protected DatagramChannel createDatagramChannel() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        return channel;
    }

    /**
     * Creates a UDP connection.
     *
     * @param channel The datagram channel.
     * @return The UDP connection.
     */
    protected UDPConnection createUDPConnection(DatagramChannel channel) {
        return new UDPConnection(channel, true);
    }

    /**
     * Sends a packet through the server's connection.
     *
     * @param packet The packet to send.
     * @throws SendFailedException If the packet fails to send.
     */
    public void send(BasePacket packet) throws SendFailedException {
        if (connection != null) {
            connection.enqueuePacket(packet);
        } else {
            throw new SendFailedException("No connection available to send the packet.");
        }
    }

    /**
     * Stops the server and closes its connection.
     */
    @Override
    public void stop() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Interrupts the server, effectively stopping it.
     */
    @Override
    public void interrupt() {
        stop();
    }

    /**
     * Retrieves the IP of the server.
     *
     * @return The IP of the server.
     */
    public String getIp() {
        return connection != null ? connection.getIp() : "";
    }

    /**
     * Retrieves the port of the server.
     *
     * @return The port of the server.
     */
    public int getPort() {
        return connection != null ? connection.getPort() : 0;
    }

    /**
     * Checks if the server is connected.
     *
     * @return True if the server is connected, false otherwise.
     */
    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * Executes the server's main loop.
     */
    @Override
    public void execute() {
        if (connection != null) {
            connection.execute();
        }
    }

    /**
     * Retrieves the server's thread timeout.
     *
     * @return The server's thread timeout.
     */
    @Override
    public long getTimeout() {
        return 1; // Example implementation detail
    }

    /**
     * Retrieves the server's thread priority.
     *
     * @return The server's thread priority.
     */
    @Override
    public int getPriority() {
        return 0; // Example implementation detail
    }

    /**
     * Retrieves the server's connection.
     *
     * @return The server's connection.
     */
    public UDPConnection getConnection() {
        return connection;
    }
}
