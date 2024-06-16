package com.han.towersofivory.network.businesslayer;

import com.han.towersofivory.network.businesslayer.connection.BaseConnection;
import com.han.towersofivory.network.businesslayer.connection.IConnectionHandler;
import com.han.towersofivory.network.businesslayer.connection.TCPConnection;
import com.han.towersofivory.network.businesslayer.connection.UDPConnection;
import com.han.towersofivory.network.businesslayer.discovery.IClientDiscovery;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.DispatchPacketException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.listeners.ClientConnectionListener;
import com.han.towersofivory.network.businesslayer.listeners.ClientDiscoveryListener;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketDispatcher;
import com.han.towersofivory.network.businesslayer.packet.PacketSource;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.server.BaseUDPServer;
import com.han.towersofivory.network.businesslayer.threading.ThreadManager;
import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;
import com.han.towersofivory.network.dto.HostInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Queue;

/**
 * P2PClient is a class that implements the IOperation interface.
 * It represents a peer-to-peer client in the network.
 * This class handles the discovery of hosts, sending and receiving of packets, and connection to the host.
 */
public class P2PClient implements IOperation {
    private static final Logger LOGGER = LogManager.getLogger(P2PClient.class);
    private final IClientDiscovery clientDiscovery;
    private final ThreadManager threadManager;
    private final PacketDispatcher clientPacketDispatcher;
    private ClientConnectionListener clientConnectionPacketListener;
    private ClientDiscoveryListener discoveryResponsePacketListener;
    private BaseConnection tcpConnection;

    /**
     * Constructor for the P2PClient class.
     * Initializes the client with a thread manager and a discovery mechanism.
     *
     * @param threadManager The thread manager to be used by the client.
     * @param discovery     The discovery mechanism to be used by the client.
     */
    public P2PClient(ThreadManager threadManager, IClientDiscovery discovery) {
        this.clientDiscovery = discovery;
        this.threadManager = threadManager;
        this.clientPacketDispatcher = new PacketDispatcher();
    }

    /**
     * Registers listeners for client connection and discovery response.
     */
    private void registerListeners() {
        clientConnectionPacketListener = new ClientConnectionListener(tcpConnection);
        discoveryResponsePacketListener = new ClientDiscoveryListener(clientDiscovery);

        clientPacketDispatcher.register(clientConnectionPacketListener);
        clientPacketDispatcher.register(discoveryResponsePacketListener);
    }

    /**
     * Unregisters listeners for client connection and discovery response.
     */
    private void unregisterListeners() {
        clientPacketDispatcher.unregister(clientConnectionPacketListener);
        clientPacketDispatcher.unregister(discoveryResponsePacketListener);

        discoveryResponsePacketListener = null;
        clientConnectionPacketListener = null;
    }

    /**
     * Sends a packet through the TCP connection.
     *
     * @param packet The packet to be sent.
     */
    public void sendPacket(BasePacket packet) {
        try {
            packet.setSource(PacketSource.CLIENT);
            tcpConnection.send(packet);
        } catch (SendFailedException e) {
            LOGGER.info("Failed to enqueue packet for sending", e);
        }
    }

    /**
     * Registers a packet listener.
     *
     * @param packetListener The packet listener to be registered.
     */
    public void registerPacketListener(IPacketListener packetListener) {
        clientPacketDispatcher.register(packetListener);
    }

    /**
     * UC4 Deelnemen Spel
     *
     * Connects to a host.
     *
     * @param hostAddress The address of the host to connect to.
     * @throws ConnectionFailedException If the connection to the host fails.
     */
    public void connectToHost(InetSocketAddress hostAddress) throws ConnectionFailedException {
        if (tcpConnection != null && tcpConnection.isConnected()) {
            return;
        }

        try {
            SocketChannel socket = SocketChannel.open();

            tcpConnection = new TCPConnection(socket, true, new IConnectionHandler() {
                @Override
                public void onTimeout(BaseConnection baseConnection, Exception e) {
                    disconnectFromHost();
                    LOGGER.info("Timed-out from host {}", baseConnection.getIp());
                }

                @Override
                public void onDisconnect(BaseConnection baseConnection, Exception e) {
                    LOGGER.info("Disconnected from to host {}", baseConnection.getIp());
                    stopClientOperations();
                }

                @Override
                public void onConnect(BaseConnection baseConnection, Exception e) {
                    LOGGER.info("Connected to host {}", baseConnection.getIp());

                    ConnectionPacket connectionPacket = new ConnectionPacket(baseConnection.getUuid(), baseConnection.getIp());
                    baseConnection.enqueuePacket(connectionPacket);
                }

                @Override
                public void onReceiveFailed(BaseConnection baseConnection, Exception e) {
                    LOGGER.info("Failed to receive packet from host {}", baseConnection.getIp(), e);
                }

                @Override
                public void onSendFailed(BaseConnection baseConnection, Exception e) {
                    LOGGER.info("Failed to send packet to host {}", baseConnection.getIp());
                }
            });

            tcpConnection.connect(hostAddress);

            registerListeners();
            threadManager.runScheduledOperation(this);
            threadManager.runScheduledOperation(tcpConnection);
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to connect to host");
        }
    }

    /**
     * Stops the operation logic from executing
     */
    public void stopClientOperations() {
        threadManager.stopOperation(tcpConnection);
        threadManager.stopOperation(this);
    }

    /**
     * Disconnects from a host.
     */
    public void disconnectFromHost() {
        if (tcpConnection == null) {
            return;
        }

        tcpConnection.close();
        tcpConnection = null;

        unregisterListeners();

        stopClientOperations();
    }

    /**
     * Discovers hosts.
     *
     * @param hostAddress The address of the host to discover.
     * @param timeout     The timeout for the discovery operation.
     */
    public void discoverHosts(InetSocketAddress hostAddress, int timeout) {
        try {
            clientDiscovery.getHosts().clear();
            registerListeners();
            threadManager.runScheduledOperation(this);
            clientDiscovery.discoverHosts(hostAddress);
            Thread.sleep(timeout);
            clientDiscovery.stopHostDiscovery();
            threadManager.stopOperation(this);
            unregisterListeners();
        } catch (SendFailedException | ConnectionFailedException e) {
            LOGGER.error("Discovery failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            LOGGER.error("Discovery was interrupted", e);
        }
    }

    @Override
    public void execute() {
        try {
            handleClientDiscovery();
            handleTcpConnection();
        } catch (SendFailedException e) {
            LOGGER.info("Failed to send packet", e);
        } catch (DispatchPacketException e) {
            LOGGER.info("Failed to map packet", e);
        }
    }

    /**
     * Handles the client discovery process.
     * This includes processing incoming packets and sending outgoing packets.
     * This is a private method used internally to manage the discovery process.
     *
     * @throws SendFailedException     If sending a packet fails.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void handleClientDiscovery() throws SendFailedException, DispatchPacketException {
        if (clientDiscovery == null) {
            return;
        }

        BaseUDPServer udpServer = clientDiscovery.getDiscoverClient();
        if (udpServer == null) {
            return;
        }

        UDPConnection discoveryConnection = udpServer.getConnection();
        if (discoveryConnection == null) {
            return;
        }

        processPackets(discoveryConnection.getIncomingQueue());
        sendPackets(discoveryConnection.getOutgoingQueue(), discoveryConnection);
    }

    /**
     * Handles the TCP connection process.
     * This includes processing incoming packets and sending outgoing packets.
     * This is a private method used internally to manage the TCP connection process.
     *
     * @throws SendFailedException     If sending a packet fails.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void handleTcpConnection() throws SendFailedException, DispatchPacketException {
        if (tcpConnection == null) {
            return;
        }

        processPackets(tcpConnection.getIncomingQueue());
        sendPackets(tcpConnection.getOutgoingQueue(), tcpConnection);
    }

    /**
     * Processes the packets in the given queue.
     * This includes dispatching each packet in the queue.
     * This is a private method used internally to manage packet processing.
     *
     * @param incomingPackets The queue of incoming packets to be processed.
     * @throws DispatchPacketException If dispatching a packet fails.
     */
    private void processPackets(Queue<BasePacket> incomingPackets) throws DispatchPacketException {
        while (!incomingPackets.isEmpty()) {
            BasePacket packet = incomingPackets.poll();
            if (packet.isFromClient()) {
                continue;
            }
            clientPacketDispatcher.dispatch(packet);
        }
    }

    /**
     * Sends the packets in the given queue.
     * This includes sending each packet in the queue through the given connection.
     * This is a private method used internally to manage packet sending.
     *
     * @param outgoingPackets The queue of outgoing packets to be sent.
     * @param connection      The connection through which the packets will be sent.
     * @throws SendFailedException If sending a packet fails.
     */
    private void sendPackets(Queue<BasePacket> outgoingPackets, BaseConnection connection) throws SendFailedException {
        while (!outgoingPackets.isEmpty()) {
            BasePacket packet = outgoingPackets.poll();
            packet.setSource(PacketSource.CLIENT);
            connection.send(packet);
        }
    }

    /**
     * Gets the hosts.
     *
     * @return The hosts.
     */
    public List<HostInformation> getHosts() {
        return clientDiscovery.getHosts();
    }

    /**
     * Interrupts the operation.
     */
    @Override
    public void interrupt() {
        disconnectFromHost();
    }

    /**
     * Gets the thread timeout.
     *
     * @return The thread timeout.
     */
    @Override
    public long getTimeout() {
        return 1;
    }

    /**
     * Gets the thread priority.
     *
     * @return The thread priority.
     */
    @Override
    public int getPriority() {
        return 0;
    }
}
