package com.han.towersofivory.network.businesslayer.connection;

import com.google.gson.JsonObject;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

/**
 * UDPConnection is a class that extends the BaseConnection abstract class.
 * It represents a UDP connection in the network.
 * This class handles the sending and receiving of packets, and connection management.
 */
public class UDPConnection extends BaseConnection {
    private static final Logger LOGGER = LogManager.getLogger(UDPConnection.class);
    private final DatagramChannel datagramChannel;
    private final ByteBuffer buffer;

    /**
     * Constructor for the UDPConnection class.
     *
     * @param datagramChannel The DatagramChannel to be used by the connection.
     * @param configure       Whether the socket should be configured or not.
     * @param callback        The callback interface for connection events.
     */
    public UDPConnection(DatagramChannel datagramChannel, boolean configure, IConnectionHandler callback) {
        super(callback);

        this.datagramChannel = datagramChannel;
        this.buffer = ByteBuffer.allocate(8096);

        configureDatagramChannel(configure);

        notifyConnect();
    }

    /**
     * Constructor for the UDPConnection class.
     *
     * @param datagramChannel The DatagramChannel to be used by the connection.
     * @param configure       Whether the socket should be configured or not.
     */
    public UDPConnection(DatagramChannel datagramChannel, boolean configure) {
        this(datagramChannel, configure, null);
    }

    /**
     * Sends a packet through the connection.
     *
     * @param packet The packet to be sent.
     */
    @Override
    public void doSend(BasePacket packet) {
        if (!isConnected()) {
            handleSendException(new ClosedChannelException());
        }

        if (packet == null) {
            handleSendException(new SendFailedException("Packet cannot be null."));
            return;
        }

        try {
            JsonObject packetJson = createPacketJson(packet);
            sendRawPacket(jsonToJSONString(packetJson));
            LOGGER.info("Packet sent: {}", packet.getClass().getSimpleName());
        } catch (Exception e) {
            handleSendException(e);
        }
    }

    /**
     * Receives a packet from the connection.
     */
    @Override
    public String receive() {
        try {
            if (!isConnected()) {
                throw new ClosedChannelException();
            }

            buffer.clear();
            SocketAddress sender = datagramChannel.receive(buffer);
            if (sender == null) {
                return null;
            }
            buffer.flip();
            return new String(buffer.array(), 0, buffer.limit());
        }  catch (IOException e) {
            handleReceiveException(e);
        }
        return null;
    }

    /**
     * Establishes a connection to the specified host address.
     *
     * @param hostAddress The IP address and port number of the host to connect to.
     * @throws ConnectionFailedException If an error occurs when attempting to connect.
     */
    @Override
    public void connect(InetSocketAddress hostAddress) throws ConnectionFailedException {
        try {
            datagramChannel.connect(hostAddress);
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to establish connection", e);
        }
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        closeConnection();
    }

    /**
     * Checks if the connection is connected.
     *
     * @return True if the connection is connected, false otherwise.
     */
    @Override
    public boolean isConnected() {
        return datagramChannel != null && datagramChannel.isOpen();
    }

    /**
     * Gets the IP of the connection.
     *
     * @return The IP of the connection.
     */
    @Override
    public String getIp() {
        try {
            if (isConnected() && datagramChannel.getLocalAddress() != null) {
                return ((InetSocketAddress) datagramChannel.getLocalAddress()).getAddress().getHostAddress();
            }
        } catch (IOException ignored) {
            LOGGER.info("Failed to get IP");
        }
        return "0.0.0.0";
    }

    /**
     * Gets the port of the connection.
     *
     * @return The port of the connection.
     */
    @Override
    public int getPort() {
        try {
            if (isConnected() && datagramChannel.getLocalAddress() != null) {
                return ((InetSocketAddress) datagramChannel.getLocalAddress()).getPort();
            }
        } catch (IOException ignored) {
            LOGGER.info("Failed to get port");
        }
        return 0;
    }

    /**
     * Configures the datagram channel.
     *
     * @param configure Whether the socket should be configured or not.
     */
    private void configureDatagramChannel(boolean configure) {
        try {
            if (configure) {
                datagramChannel.socket().setSoTimeout(2000);
                datagramChannel.configureBlocking(false);
                datagramChannel.socket().setReuseAddress(true);
                datagramChannel.socket().setReceiveBufferSize(8192);
                datagramChannel.socket().setSendBufferSize(8192);
                datagramChannel.socket().setTrafficClass(0x18);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to initialize UDP connection.", e);
        }
    }

    /**
     * Sends a raw JSON packet through the datagram channel.
     *
     * @param rawPacket The raw JSON packet string to be sent.
     * @throws IOException If an I/O error occurs.
     */
    private void sendRawPacket(String rawPacket) throws IOException {
        if (!isConnected()) {
            return;
        }

        byte[] rawBytes = (rawPacket + System.lineSeparator()).getBytes();
        buffer.clear();
        buffer.put(rawBytes);
        buffer.flip();
        datagramChannel.write(buffer);
    }

    /**
     * Closes the datagram channel.
     */
    private void closeConnection() {
        if (!isConnected()) {
            return;
        }

        try {
            datagramChannel.close();
            LOGGER.info("UDP connection closed");
            notifyDisconnect();
        } catch (IOException e) {
            LOGGER.error("Failed to close UDP connection properly", e);
        }
    }
}
