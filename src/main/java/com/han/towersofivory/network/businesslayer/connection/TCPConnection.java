package com.han.towersofivory.network.businesslayer.connection;

import com.google.gson.JsonObject;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * TCPConnection is a class that extends the BaseConnection abstract class.
 * It represents a TCP connection in the network.
 * This class handles the sending and receiving of packets, and connection management.
 */
public class TCPConnection extends BaseConnection {
    private static final Logger LOGGER = LogManager.getLogger(TCPConnection.class);
    private final ByteBuffer readBuffer;
    private final ByteBuffer writeBuffer;
    private SocketChannel socketChannel;

    /**
     * Constructor for the TCPConnection class.
     *
     * @param socketChannel The SocketChannel to be used by the connection.
     * @param configure     Whether the socket should be configured or not.
     * @param callback      The callback interface for connection events.
     * @throws ConnectionFailedException If an I/O error occurs.
     */
    public TCPConnection(SocketChannel socketChannel, boolean configure, IConnectionHandler callback) throws ConnectionFailedException {
        super(callback);

        this.socketChannel = socketChannel;
        this.readBuffer = ByteBuffer.allocate(8096);
        this.writeBuffer = ByteBuffer.allocate(8096);

        configureSocketChannel(configure);
    }

    /**
     * Constructor for the TCPConnection class.
     *
     * @param socketChannel The SocketChannel to be used by the connection.
     * @param isBlocking    Whether the socket should be blocking or non-blocking.
     * @throws ConnectionFailedException If an I/O error occurs.
     */
    public TCPConnection(SocketChannel socketChannel, boolean isBlocking) throws ConnectionFailedException {
        this(socketChannel, isBlocking, null);
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
            socketChannel.connect(hostAddress);

            long startTime = System.currentTimeMillis();
            while (!socketChannel.finishConnect()) {
                if (System.currentTimeMillis() - startTime >= socketChannel.socket().getSoTimeout()) {
                    throw new ConnectionFailedException("Connection timed out");
                }
            }
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to establish connection", e);
        }
    }

    /**
     * Sends a packet through the connection.
     *
     * @param packet The packet to be sent.
     */
    @Override
    protected synchronized void doSend(BasePacket packet) {
        if (!isConnected()) {
            handleSendException(new ClosedChannelException());
            return;
        }

        if (packet == null) {
            handleSendException(new SendFailedException("Packet cannot be null."));
            return;
        }

        try {
            JsonObject packetJson = createPacketJson(packet);
            sendRawPacket(socketChannel, writeBuffer, jsonToJSONString(packetJson));
            LOGGER.debug("Packet sent: {}", packet.getClass().getSimpleName());
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

            readBuffer.clear();

            int numRead = socketChannel.read(readBuffer);
            if (numRead == -1) {
                return null;
            }

            readBuffer.flip();
            byte[] dataBytes = new byte[numRead];
            readBuffer.get(dataBytes);
            return new String(dataBytes, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            handleReceiveException(e);
        }
        return null;
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        closeConnection();
        socketChannel = null;
    }

    /**
     * Checks if the connection is connected.
     *
     * @return True if the connection is connected, false otherwise.
     */
    @Override
    public boolean isConnected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    /**
     * Gets the IP of the connection.
     *
     * @return The IP of the connection.
     */
    @Override
    public String getIp() {
        try {
            return isConnected() && socketChannel.getLocalAddress() != null ?
                    socketChannel.getLocalAddress().toString() : "0.0.0.0";
        } catch (IOException e) {
            LOGGER.info("Failed to get ip");
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
            return isConnected() && socketChannel.getLocalAddress() != null ?
                    ((InetSocketAddress) socketChannel.getLocalAddress()).getPort() : 0;
        } catch (IOException e) {
            LOGGER.info("Failed to get port");
        }
        return 0;
    }

    /**
     * Configures the socket channel.
     *
     * @param configure Whether the socket should be configured or not
     */
    private void configureSocketChannel(boolean configure) {
        try {
            if (configure) {
                socketChannel.socket().setSoTimeout(500);
                socketChannel.socket().setTrafficClass(0x4);
                socketChannel.socket().setTcpNoDelay(true);
                socketChannel.socket().setKeepAlive(false);
                socketChannel.socket().setOOBInline(false);
                socketChannel.socket().setPerformancePreferences(2, 1, 0);
                socketChannel.socket().setReceiveBufferSize(8192);
                socketChannel.socket().setSendBufferSize(8192);
                socketChannel.socket().setReuseAddress(true);
                socketChannel.socket().setSoLinger(true, 500);
                socketChannel.configureBlocking(false);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to initialize TCP connection.", e);
        }
    }

    /**
     * Sends a raw JSON packet through the socket channel.
     *
     * @param socketChannel The SocketChannel to send the packet through.
     * @param writeBuffer   The ByteBuffer used for writing.
     * @param rawPacket     The raw JSON packet string to be sent.
     * @throws IOException If an I/O error occurs.
     */
    private void sendRawPacket(SocketChannel socketChannel, ByteBuffer writeBuffer, String rawPacket) throws IOException {
        if (!isConnected()) {
            return;
        }
        byte[] rawBytes = (rawPacket + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
        writeBuffer.clear();
        writeBuffer.put(rawBytes);
        writeBuffer.flip();
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
    }

    /**
     * Closes the socket channel.
     */
    private void closeConnection() {
        if (!isConnected()) {
            return;
        }

        try {
            socketChannel.close();
            LOGGER.info("TCP connection closed successfully.");
            notifyDisconnect();
        } catch (IOException e) {
            LOGGER.error("Failed to close TCP connection properly", e);
        }
    }
}
