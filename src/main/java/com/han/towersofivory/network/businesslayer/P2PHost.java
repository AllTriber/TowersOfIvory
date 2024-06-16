package com.han.towersofivory.network.businesslayer;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.connection.BaseConnection;
import com.han.towersofivory.network.businesslayer.connection.UDPConnection;
import com.han.towersofivory.network.businesslayer.discovery.IHostDiscovery;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.DispatchPacketException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.listeners.HostDiscoveryListener;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketDispatcher;
import com.han.towersofivory.network.businesslayer.packet.PacketSource;
import com.han.towersofivory.network.businesslayer.server.BaseUDPServer;
import com.han.towersofivory.network.businesslayer.server.TCPServer;
import com.han.towersofivory.network.businesslayer.threading.ThreadManager;
import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.UUID;

/**
 * P2PHost is a class that implements the IOperation interface.
 * It represents a peer-to-peer host in the network.
 * This class handles the discovery of clients, sending and receiving of packets, and connection to the clients.
 */
public class P2PHost implements IOperation {
    private static final Logger LOGGER = LogManager.getLogger(P2PHost.class);

    private final IHostDiscovery hostDiscovery;
    private final ThreadManager threadManager;
    private final PacketDispatcher serverPacketDispatcher;
    private HostDiscoveryListener discoveryPacketListener;
    private TCPServer hostServer;

    /**
     * Constructor for the P2PHost class.
     * Initializes the host with a thread manager and a discovery mechanism.
     *
     * @param threadManager The thread manager to be used by the host.
     * @param hostDiscovery The discovery mechanism to be used by the host.
     */
    public P2PHost(ThreadManager threadManager, IHostDiscovery hostDiscovery) {
        this.hostDiscovery = hostDiscovery;
        this.threadManager = threadManager;
        this.serverPacketDispatcher = new PacketDispatcher();
    }

    /**
     * Registers listeners for host discovery.
     */
    private void registerListeners() {
        discoveryPacketListener = new HostDiscoveryListener(hostDiscovery);
        serverPacketDispatcher.register(discoveryPacketListener);
    }

    /**
     * Unregisters listeners for host discovery.
     */
    private void unregisterListeners() {
        serverPacketDispatcher.unregister(discoveryPacketListener);
        discoveryPacketListener = null;
    }

    /**
     * Broadcasts a packet to all connected clients.
     *
     * @param packet The packet to be broadcasted.
     */
    public void broadcastPacket(BasePacket packet) {
        packet.setSource(PacketSource.SERVER);
        hostServer.broadcast(packet);
    }

    /**
     * Sends a packet to a specific client.
     *
     * @param uuid   The UUID of the client to send the packet to.
     * @param packet The packet to be sent.
     */
    public void sendPacketToClient(UUID uuid, BasePacket packet) {
        packet.setSource(PacketSource.SERVER);
        hostServer.send(uuid, packet);
    }

    /**
     * Registers a packet listener.
     *
     * @param packetListener The packet listener to be registered.
     */
    public void registerPacketListener(IPacketListener packetListener) {
        serverPacketDispatcher.register(packetListener);
    }

    /**
     * Starts the server and begins listening for connections.
     *
     * @param hostAddress The address of the host to start the server on.
     * @param gameInfo    The game information to be used by the server.
     * @throws ConnectionFailedException If the server fails to start.
     */
    public void startServer(InetSocketAddress hostAddress, GameInfo gameInfo) throws ConnectionFailedException {
        if (hostServer != null) {
            return;
        }

        hostServer = new TCPServer();
        hostServer.start(hostAddress);

        registerListeners();

        hostDiscovery.allowHostDiscovery(hostAddress, gameInfo);

        threadManager.runScheduledOperation(hostServer);
        threadManager.runScheduledOperation(this);

        LOGGER.info("Network hosting started");
    }

    /**
     * Updates the game information.
     *
     * @param gameInfo The game information to be used for host discovery.
     */
    public void updateGameInfo(GameInfo gameInfo) {
        gameInfo.setNumberOfPlayers(this.connectedClientsCount());
        hostDiscovery.updateGameInfo(gameInfo);
    }

    /**
     * Stops the server and disconnects all clients.
     */
    public void stopServer() {
        if (hostServer == null) {
            return;
        }

        hostDiscovery.disableHostDiscovery();
        unregisterListeners();

        threadManager.stopOperation(hostServer);
        hostServer.stop();

        threadManager.stopOperation(this);
        hostServer = null;

        LOGGER.info("Network hosting stopped");
    }

    public int connectedClientsCount() {
        return hostServer.getConnections().size();
    }

    /**
     * Interrupts the operation.
     */
    @Override
    public void interrupt() {
        stopServer();
    }

    /**
     * Executes the operation.
     */
    @Override
    public void execute() {
        try {
            handleHostDiscovery();
            handleHostServer();
        } catch (SendFailedException e) {
            LOGGER.info("Failed to send packet", e);
        } catch (DispatchPacketException e) {
            LOGGER.info("Failed to map packet", e);
        }
    }

    /**
     * Handles the host discovery process.
     * This includes processing incoming packets and sending outgoing packets.
     *
     * @throws SendFailedException     If sending a packet fails.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void handleHostDiscovery() throws SendFailedException, DispatchPacketException {
        if (hostDiscovery == null) {
            return;
        }

        BaseUDPServer discoveryServer = hostDiscovery.getHostDiscoveryServer();
        if (discoveryServer == null) {
            return;
        }

        UDPConnection discoveryConnection = discoveryServer.getConnection();
        if (discoveryConnection == null) {
            return;
        }

        processPackets(discoveryConnection.getIncomingQueue());
        sendPackets(discoveryConnection.getOutgoingQueue(), discoveryConnection);
    }

    /**
     * Handles the host server process.
     * This includes processing incoming packets and sending outgoing packets.
     *
     * @throws SendFailedException     If sending a packet fails.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void handleHostServer() throws SendFailedException, DispatchPacketException {
        if (hostServer == null) {
            return;
        }

        for (BaseConnection connection : hostServer.getConnections().values()) {
            processPackets(connection.getIncomingQueue());
            sendPackets(connection.getOutgoingQueue(), connection);
        }
    }

    /**
     * Processes the packets in the given queue.
     * This includes dispatching each packet in the queue.
     *
     * @param incomingPackets The queue of incoming packets to be processed.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void processPackets(Queue<BasePacket> incomingPackets) throws DispatchPacketException {
        while (!incomingPackets.isEmpty()) {
            BasePacket packet = incomingPackets.poll();
            if (packet.isFromServer()) {
                continue;
            }
            serverPacketDispatcher.dispatch(packet);
        }
    }

    /**
     * Sends the packets in the given queue.
     * This includes sending each packet in the queue through the given connection.
     *
     * @param outgoingPackets The queue of outgoing packets to be sent.
     * @param connection      The connection through which the packets will be sent.
     * @throws SendFailedException If sending a packet fails.
     */
    private void sendPackets(Queue<BasePacket> outgoingPackets, BaseConnection connection) throws SendFailedException {
        while (!outgoingPackets.isEmpty()) {
            BasePacket packet = outgoingPackets.poll();
            packet.setSource(PacketSource.SERVER);
            connection.send(packet);
        }
    }

    /**
     * Gets the timeout for the thread.
     *
     * @return The timeout for the thread.
     */
    @Override
    public long getTimeout() {
        return 1;
    }

    /**
     * Gets the priority of the thread.
     *
     * @return The priority of the thread.
     */
    @Override
    public int getPriority() {
        return 0;
    }
}
