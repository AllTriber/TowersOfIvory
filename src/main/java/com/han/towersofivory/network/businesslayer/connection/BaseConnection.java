package com.han.towersofivory.network.businesslayer.connection;

import com.google.gson.*;
import com.han.towersofivory.game.businesslayer.entities.items.Item;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.ReceiveFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * BaseConnection is an abstract class that implements the IOperation interface.
 * It represents a base connection in the network.
 * This class handles the sending and receiving of packets, and connection management.
 */
public abstract class BaseConnection implements IOperation {
    private static Gson gson;
    private final IConnectionHandler connectionHandler;
    private final Queue<BasePacket> incomingQueue;
    private final Queue<BasePacket> outgoingQueue;
    private UUID uuid;

    /**
     * Constructor for the BaseConnection class.
     * Initializes the incoming and outgoing packet queues.
     */
    protected BaseConnection(IConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.incomingQueue = new ConcurrentLinkedQueue<>();
        this.outgoingQueue = new ConcurrentLinkedQueue<>();

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Item.class, (JsonSerializer<Item>) (src, typeOfSrc, context) -> {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("type", src.getClass().getName());
            wrapper.add("data", context.serialize(src));
            return wrapper;
        });

        gsonBuilder.registerTypeAdapter(Item.class, new JsonDeserializer<Item>() {
            @Override
            public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject packetJson = json.getAsJsonObject();
                Class<?> itemType;

                try {
                    itemType = Class.forName(packetJson.get("type").getAsString());
                } catch (ClassNotFoundException cnfe) {
                    throw new JsonParseException(cnfe);
                }

                return context.deserialize(packetJson.get("data"), itemType);
            }
        });

        gson = gsonBuilder.create();
    }


    /**
     * Establishes a connection to the specified host address.
     *
     * @param hostAddress The IP address and port number of the host to connect to.
     * @throws ConnectionFailedException If an error occurs when attempting to connect.
     */
    public abstract void connect(InetSocketAddress hostAddress) throws ConnectionFailedException;

    /**
     * Enqueues a packet to the outgoing queue.
     *
     * @param packet The packet to be enqueued.
     */
    public void enqueuePacket(BasePacket packet) {
        outgoingQueue.offer(packet);
    }

    /**
     * Sends a packet through the connection.
     *
     * @param packet The packet to be sent.
     * @throws SendFailedException If the packet fails to be sent.
     */
    public void send(BasePacket packet) throws SendFailedException {
        validatePacket(packet);
        if (uuid != null) {
            packet.setUuid(uuid);
        }
        doSend(packet);
    }

    /**
     * Executes the operation.
     */
    @Override
    public void execute() {
        handleReceive();
    }

    /**
     * Receives a packet from the connection.
     *
     * @throws ReceiveFailedException If the packet fails to be received.
     */
    public abstract String receive() throws ReceiveFailedException;

    /**
     * Closes the connection.
     */
    public abstract void close();

    /**
     * Checks if the connection is connected.
     *
     * @return True if the connection is connected, false otherwise.
     */
    public abstract boolean isConnected();

    /**
     * Gets the IP of the connection.
     *
     * @return The IP of the connection.
     */
    public abstract String getIp();

    /**
     * Gets the port of the connection.
     *
     * @return The port of the connection.
     */
    public abstract int getPort();

    /**
     * Gets the UUID of the connection.
     *
     * @return The UUID of the connection.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Accept the connection and set the uuid for it
     *
     * @param uuid The UUID to be set for the accepted connection
     */
    public void accept(UUID uuid) {
        this.uuid = uuid;
        notifyConnect();
    }

    /**
     * Sends a packet through the connection.
     *
     * @param packet The packet to be sent.
     * @throws SendFailedException If the packet fails to be sent.
     */
    protected abstract void doSend(BasePacket packet) throws SendFailedException;

    /**
     * Gets the incoming packet queue.
     *
     * @return The incoming packet queue.
     */
    public Queue<BasePacket> getIncomingQueue() {
        return incomingQueue;
    }

    /**
     * Gets the outgoing packet queue.
     *
     * @return The outgoing packet queue.
     */
    public Queue<BasePacket> getOutgoingQueue() {
        return outgoingQueue;
    }

    /**
     * Gets the timeout for the operation.
     *
     * @return The timeout for the operation.
     */
    @Override
    public long getTimeout() {
        return 1;
    }

    /**
     * Gets the priority of the operation.
     *
     * @return The priority of the operation.
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Interrupts the operation.
     */
    @Override
    public void interrupt() {
        close();
    }

    /**
     * Validates if the packet is not null.
     *
     * @param packet The packet to be validated.
     */
    private void validatePacket(BasePacket packet) throws SendFailedException {
        if (packet == null) {
            throw new SendFailedException("Packet cannot be null.");
        }
    }

    /**
     * Creates a JSON object from a packet.
     *
     * @param packet The packet to be converted to JSON.
     * @return The JSON object representing the packet.
     */
    protected JsonObject createPacketJson(BasePacket packet) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", packet.getClass().getName());
        wrapper.add("data", gson.toJsonTree(packet));
        return wrapper;
    }

    /**
     * Converts a json object into a JSON string.
     *
     * @param packetJson The JSON to be converted into a JSON format string.
     * @return The JSON string representation of the input JSON object.
     */
    protected String jsonToJSONString(JsonObject packetJson) {
        return gson.toJson(packetJson);
    }

    /**
     * Parses a raw JSON packet string into a BasePacket object.
     *
     * @param rawPacket The raw JSON packet string.
     * @return The parsed BasePacket object.
     * @throws ClassNotFoundException If the class of the packet cannot be found.
     */
    protected BasePacket parsePacket(String rawPacket) throws ClassNotFoundException, JsonSyntaxException {
        if (rawPacket == null || rawPacket.isEmpty()) {
            return null;
        }
        JsonObject packetJson = gson.fromJson(rawPacket, JsonObject.class);
        Class<?> packetType = Class.forName(packetJson.get("type").getAsString());
        return (BasePacket) gson.fromJson(packetJson.get("data"), packetType);
    }

    /**
     * Handles the receives process and enqueues received packets.
     */
    private void handleReceive() {
        if (!isConnected()) {
            handleReceiveException(new ClosedChannelException());
            return;
        }

        try {
            String rawPacket = receive();
            if (rawPacket == null || rawPacket.isEmpty()) {
                return;
            }

            String[] packets = rawPacket.split(System.lineSeparator(), -1);
            for (String packet : packets) {
                if(packet.isEmpty()) {
                    continue;
                }

                BasePacket basePacket = parsePacket(packet);
                if(basePacket != null) {
                    incomingQueue.add(basePacket);
                }
            }
        } catch (Exception e) {
            handleReceiveException(e);
        }
    }

    /**
     * Notifies the connection handler about a timeout event.
     *
     * @param exception The exception related to the timeout event.
     */
    protected void notifyTimeout(Exception exception) {
        if (connectionHandler == null)
            return;

        connectionHandler.onTimeout(this, exception);
    }

    /**
     * Notifies the connection handler about a disconnect event.
     */
    protected void notifyDisconnect() {
        if (connectionHandler == null)
            return;

        connectionHandler.onDisconnect(this, null);
    }

    /**
     * Notifies the connection handler about a connect event.
     */
    protected void notifyConnect() {
        if (connectionHandler == null)
            return;

        connectionHandler.onConnect(this, null);
    }

    /**
     * Notifies the connection handler about a send failure event.
     *
     * @param exception The exception related to the send failure event.
     */
    protected void notifySendFailed(Exception exception) {
        if (connectionHandler == null)
            return;

        connectionHandler.onSendFailed(this, exception);
    }

    /**
     * Notifies the connection handler about a received failure event.
     *
     * @param exception The exception related to the received failure event.
     */
    protected void notifyReceivedFailed(Exception exception) {
        if (connectionHandler == null)
            return;

        connectionHandler.onReceiveFailed(this, exception);
    }

    /**
     * Handles exceptions thrown during the send or receive process.
     *
     * @param e The exception to handle.
     */
    protected void handleReceiveException(Exception e) {
        if (e instanceof SocketTimeoutException) {
            notifyTimeout(e);
        } else if (e instanceof ClosedChannelException || e instanceof NotYetConnectedException || (e instanceof SocketException socketException && socketException.getMessage().equalsIgnoreCase("Connection reset"))) {
            close();
        } else {
            notifyReceivedFailed(e);
        }
    }

    /**
     * Handles exceptions thrown during the send or receive process.
     *
     * @param e The exception to handle.
     */
    protected void handleSendException(Exception e) {
        if (e instanceof SocketTimeoutException) {
            notifyTimeout(e);
        } else if (e instanceof ClosedChannelException || e instanceof NotYetConnectedException || (e instanceof SocketException socketException && socketException.getMessage().equalsIgnoreCase("Connection reset"))) {
            close();
        } else {
            notifySendFailed(e);
        }
    }
}
