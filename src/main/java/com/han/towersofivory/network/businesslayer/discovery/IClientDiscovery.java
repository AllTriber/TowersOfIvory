package com.han.towersofivory.network.businesslayer.discovery;

import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.server.BaseUDPServer;
import com.han.towersofivory.network.dto.HostInformation;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Interface for client discovery operations.
 * Provides methods for discovering hosts, stopping host discovery, getting discovered hosts, and getting the discovery client.
 */
public interface IClientDiscovery {

    /**
     * Discovers hosts at the given host discovery address.
     *
     * @param hostDiscoveryAddress The address to discover hosts at.
     * @throws ConnectionFailedException If the connection fails.
     * @throws SendFailedException       If sending fails.
     */
    void discoverHosts(InetSocketAddress hostDiscoveryAddress) throws ConnectionFailedException, SendFailedException;

    /**
     * Stops the host discovery process.
     */
    void stopHostDiscovery();

    /**
     * Gets a list of discovered hosts.
     *
     * @return A list of InetSocketAddresses representing the discovered hosts.
     */
    List<HostInformation> getHosts();

    /**
     * Gets the discovery client.
     *
     * @return The BaseUDPServer representing the discovery client.
     */
    BaseUDPServer getDiscoverClient();
}