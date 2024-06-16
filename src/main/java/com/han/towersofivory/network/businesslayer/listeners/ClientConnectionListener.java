package com.han.towersofivory.network.businesslayer.listeners;

import com.han.towersofivory.network.businesslayer.connection.BaseConnection;
import com.han.towersofivory.network.businesslayer.packet.IPacketHandler;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketPriority;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClientConnectionListener class that implements the IPacketListener interface.
 * It provides methods for handling packets related to client connections.
 */
public class ClientConnectionListener implements IPacketListener {
    private static final Logger LOGGER = LogManager.getLogger(ClientConnectionListener.class);
    private final BaseConnection connection;

    /**
     * Constructor for the ClientConnectionListener class.
     *
     * @param connection The BaseConnection object representing the client's connection.
     */
    public ClientConnectionListener(BaseConnection connection) {
        this.connection = connection;
    }

    /**
     * Handles the event of a host server connection.
     *
     * @param connectionPacket The ConnectionPacket object containing information about the connection.
     */
    @IPacketHandler(priority = PacketPriority.MONITOR)
    public void onHostConnection(ConnectionPacket connectionPacket) {
        connection.accept(connectionPacket.getUuid());
        LOGGER.info("{} has accepted your connection {}", connectionPacket.getSourceAddress(), connectionPacket.getUuid());
    }
}