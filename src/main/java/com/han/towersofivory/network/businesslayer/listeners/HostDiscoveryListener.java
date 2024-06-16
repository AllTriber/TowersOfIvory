package com.han.towersofivory.network.businesslayer.listeners;

import com.han.towersofivory.network.businesslayer.discovery.IHostDiscovery;
import com.han.towersofivory.network.businesslayer.packet.IPacketHandler;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketPriority;
import com.han.towersofivory.network.businesslayer.packets.DiscoveryPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * HostDiscoveryListener class that implements the IPacketListener interface.
 * It provides methods for handling packets related to host discovery.
 */
public class HostDiscoveryListener implements IPacketListener {
    private static final Logger LOGGER = LogManager.getLogger(HostDiscoveryListener.class);
    private final IHostDiscovery hostDiscovery;

    /**
     * Constructor for the HostDiscoveryListener class.
     *
     * @param hostDiscovery The IHostDiscovery object for discovering hosts.
     */
    public HostDiscoveryListener(IHostDiscovery hostDiscovery) {
        this.hostDiscovery = hostDiscovery;
    }

    /**
     * Handles the event of a client discovery packet.
     *
     * @param discoveryPacket The DiscoveryPacket object containing information about the discovered client.
     */
    @IPacketHandler(priority = PacketPriority.MONITOR)
    public void onClientDiscoveryPacket(DiscoveryPacket discoveryPacket) {
        LOGGER.info("Discovery packet received from client: {}", discoveryPacket.getSourceAddress());

        InetSocketAddress targetSocketAddress = new InetSocketAddress(discoveryPacket.getSourceAddress(), discoveryPacket.getSourcePort());
        hostDiscovery.broadcastPresence(targetSocketAddress);
    }
}