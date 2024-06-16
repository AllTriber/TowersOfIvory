package com.han.towersofivory.network.businesslayer.discovery;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.server.BaseUDPServer;

import java.net.InetSocketAddress;

/**
 * Interface for host discovery operations.
 * Provides methods for allowing host discovery, disabling host discovery, broadcasting presence, and getting the host discovery server.
 */
public interface IHostDiscovery {

    /**
     * Allows host discovery at the given host discovery address.
     *
     * @param hostDiscoveryAddress The address to allow host discovery at.
     * @param gameInfo             The game information to be used for host discovery.
     * @throws ConnectionFailedException If the connection fails.
     */
    void allowHostDiscovery(InetSocketAddress hostDiscoveryAddress, GameInfo gameInfo) throws ConnectionFailedException;

    /**
     * Updates the game information.
     *
     * @param gameInfo The game information to be used for host discovery.
     */
    void updateGameInfo(GameInfo gameInfo);

    /**
     * Disables the host discovery process.
     */
    void disableHostDiscovery();

    /**
     * Broadcasts the presence of the host to a target machine.
     *
     * @param targetMachineAddress The address of the target machine to broadcast presence to.
     */
    void broadcastPresence(InetSocketAddress targetMachineAddress);

    /**
     * Gets the host discovery server.
     *
     * @return The BaseUDPServer representing the host discovery server.
     */
    BaseUDPServer getHostDiscoveryServer();
}