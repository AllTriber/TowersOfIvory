package com.han.towersofivory.network.businesslayer.listeners;

import com.han.towersofivory.network.businesslayer.discovery.IClientDiscovery;
import com.han.towersofivory.network.businesslayer.packet.IPacketHandler;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketPriority;
import com.han.towersofivory.network.businesslayer.packets.DiscoveryPacket;
import com.han.towersofivory.network.dto.HostInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * ClientDiscoveryListener class that implements the IPacketListener interface.
 * It provides methods for handling packets related to client discovery.
 */
public class ClientDiscoveryListener implements IPacketListener {
    private static final Logger LOGGER = LogManager.getLogger(ClientDiscoveryListener.class);
    private final IClientDiscovery discovery;

    /**
     * Constructor for the ClientDiscoveryListener class.
     *
     * @param discovery The IClientDiscovery object for discovering hosts.
     */
    public ClientDiscoveryListener(IClientDiscovery discovery) {
        this.discovery = discovery;
    }

    /**
     * Handles the event of a host discovery packet.
     *
     * @param discoveryPacket The DiscoveryPacket object containing information about the discovered host.
     */
    @IPacketHandler(priority = PacketPriority.MONITOR)
    public void onHostDiscoveryPacket(DiscoveryPacket discoveryPacket) {
        LOGGER.info("Discovery response received from host: {}", discoveryPacket.getSourceAddress());
        discovery.getHosts().add(new HostInformation(new InetSocketAddress(discoveryPacket.getSourceAddress(), discoveryPacket.getSourcePort()), discoveryPacket.getGameInfo()));
    }
}