package com.han.towersofivory.network.businesslayer.server;

import com.han.towersofivory.network.businesslayer.connection.BaseConnection;
import com.han.towersofivory.network.businesslayer.connection.IConnectionHandler;
import com.han.towersofivory.network.businesslayer.connection.TCPConnection;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.packets.DisconnectPacket;
import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TCPServer is a class that implements the IServer and IOperation interfaces.
 * It represents a TCP server that can start, stop, and execute operations.
 * It contains methods to broadcast and send packets, and to get server details.
 * It also provides a method to check if the server is connected.
 */
public class TCPServer implements IServer, IOperation {
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);
    private final ConcurrentHashMap<UUID, BaseConnection> clientConnections;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    /**
     * Constructor for the TCPServer class.
     * Initializes the clientConnections map.
     */
    public TCPServer() {
        this.clientConnections = new ConcurrentHashMap<>();
    }

    /**
     * Broadcasts a packet to all connected clients.
     *
     * @param packet The packet to broadcast.
     */
    public void broadcast(BasePacket packet) {
        clientConnections.values().forEach(connection -> {
            if (connection.isConnected()) {
                connection.enqueuePacket(packet);
            }
        });
    }

    /**
     * Sends a packet to a specific client.
     *
     * @param uuid   The UUID of the client to send the packet to.
     * @param packet The packet to send.
     */
    public void send(UUID uuid, BasePacket packet) {
        if (uuid == null)
            return;

        BaseConnection connection = clientConnections.get(uuid);
        if (connection != null && connection.isConnected()) {
            connection.enqueuePacket(packet);
        }
    }

    /**
     * Starts the server with the given address.
     *
     * @param address The address to start the server at.
     * @throws ConnectionFailedException If the server fails to start.
     */
    @Override
    public void start(InetSocketAddress address) throws ConnectionFailedException {
        try {
            serverSocketChannel = openServerSocketChannel(address);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to start TCP server", e);
        }
    }

    /**
     * Stops the server and closes all connections.
     */
    @Override
    public void stop() {
        closeAllConnections();
        closeServerSocketChannel();
        closeSelector();
        LOGGER.info("TCP server stopped");
    }

    /**
     * Interrupts the server, effectively stopping it.
     */
    @Override
    public void interrupt() {
        stop();
    }

    /**
     * Executes the server's main loop, accepting new connections and processing existing ones.
     */
    @Override
    public void execute() {
        try {
            acceptNewConnections();
            processClientConnections();
        } catch (IOException | ConnectionFailedException e) {
            LOGGER.error("TCP server socket closed or a problem occurred", e);
        }
    }

    /**
     * Retrieves the current connections to the server.
     *
     * @return A map of the current connections to the server.
     */
    public ConcurrentMap<UUID, BaseConnection> getConnections() {
        return clientConnections;
    }

    /**
     * Retrieves the server's thread timeout.
     *
     * @return The server's thread timeout.
     */
    @Override
    public long getTimeout() {
        return 1;
    }

    /**
     * Retrieves the server's thread priority.
     *
     * @return The server's thread priority.
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Retrieves the IP of the server.
     *
     * @return The IP of the server.
     */
    @Override
    public String getIp() {
        return serverSocketChannel != null ? serverSocketChannel.socket().getInetAddress().getHostAddress() : "";
    }

    /**
     * Retrieves the port of the server.
     *
     * @return The port of the server.
     */
    @Override
    public int getPort() {
        return serverSocketChannel != null ? serverSocketChannel.socket().getLocalPort() : 0;
    }

    /**
     * Checks if the server is connected.
     *
     * @return True if the server is connected, false otherwise.
     */
    @Override
    public boolean isConnected() {
        return serverSocketChannel != null && serverSocketChannel.isOpen();
    }

    /**
     * Opens and configures the server socket channel.
     *
     * @param address The address to bind the server socket to.
     * @return The configured server socket channel.
     * @throws IOException If an I/O error occurs.
     */
    private ServerSocketChannel openServerSocketChannel(InetSocketAddress address) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(address);
        channel.configureBlocking(false);
        return channel;
    }

    /**
     * Closes all client connections.
     */
    private void closeAllConnections() {
        clientConnections.values().forEach(BaseConnection::close);
        clientConnections.clear();
    }

    /**
     * Closes the server socket channel if it is open.
     */
    private void closeServerSocketChannel() {
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error closing server socket channel", e);
        }
    }

    /**
     * Closes the selector if it is open.
     */
    private void closeSelector() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error closing selector", e);
        }
    }

    /**
     * Accepts new client connections.
     *
     * @throws IOException               If an I/O error occurs.
     * @throws ConnectionFailedException If a connection fails.
     */
    private void acceptNewConnections() throws IOException, ConnectionFailedException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        if (clientChannel != null) {
            String address = clientChannel.getRemoteAddress().toString();
            String ip = address.replace("/", "").split(":")[0];
            UUID clientUUID = UUID.nameUUIDFromBytes(
                    ip.getBytes()
            );
            TCPConnection clientConnection = new TCPConnection(clientChannel, true, getConnectionHandler());
            clientConnection.accept(clientUUID);

            LOGGER.info("New TCP connection established with UUID: {}", clientUUID);
        }
    }

    /**
     * Processes existing client connections.
     */
    private void processClientConnections() {
        clientConnections.values().removeIf(connection -> {
            if (connection.isConnected()) {
                connection.execute();
                return false;
            } else {
                return true;
            }
        });
    }

    /**
     * Retrieves a connection handler with logging callbacks.
     *
     * @return The connection handler.
     */
    private IConnectionHandler getConnectionHandler() {
        return new IConnectionHandler() {
            @Override
            public void onTimeout(BaseConnection baseConnection, Exception e) {
                LOGGER.error("Client TCP connection timed-out");

                clientConnections.remove(baseConnection.getUuid());

                BasePacket disconnectPacket = new DisconnectPacket(baseConnection.getUuid(), getIp());
                broadcast(disconnectPacket);
            }

            @Override
            public void onDisconnect(BaseConnection baseConnection, Exception e) {
                LOGGER.info("Client TCP successfully disconnect");

                clientConnections.remove(baseConnection.getUuid());

                BasePacket disconnectPacket = new DisconnectPacket(baseConnection.getUuid(), getIp());
                broadcast(disconnectPacket);
            }

            @Override
            public void onConnect(BaseConnection baseConnection, Exception e) {
                LOGGER.info("Client TCP connection success");

                clientConnections.put(baseConnection.getUuid(), baseConnection);

                BasePacket connectionPacket = new ConnectionPacket(baseConnection.getUuid(), getIp());
                broadcast(connectionPacket);
            }

            @Override
            public void onReceiveFailed(BaseConnection baseConnection, Exception e) {
                LOGGER.info("Failed to receive packet from {}", baseConnection.getUuid());
            }

            @Override
            public void onSendFailed(BaseConnection baseConnection, Exception e) {
                LOGGER.info("Failed to send packet to {}", baseConnection.getUuid());
            }
        };
    }
}
