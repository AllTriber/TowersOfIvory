package com.han.towersofivory.network.businesslayer;

import com.han.towersofivory.network.businesslayer.discovery.IClientDiscovery;
import com.han.towersofivory.network.businesslayer.discovery.IHostDiscovery;
import com.han.towersofivory.network.businesslayer.discovery.LocalClientDiscovery;
import com.han.towersofivory.network.businesslayer.discovery.LocalHostDiscovery;
import com.han.towersofivory.network.businesslayer.threading.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * LocalP2PNetwork is a class that implements the IP2PNetwork interface.
 * It represents a local peer-to-peer network with a host and a client.
 * This class also handles the discovery of hosts and clients in the network.
 */
public class LocalP2PNetwork implements IP2PNetwork {
    private static final Logger LOGGER = LogManager.getLogger(LocalP2PNetwork.class);
    private static final int DISCOVERY_SERVER_PORT = 8886;
    private static final String DISCOVERY_ADDRESS = "224.0.0.1";
    private final P2PClient client;
    private final P2PHost host;

    /**
     * Constructor for the LocalP2PNetwork class.
     * Initializes the network with a host and a client.
     */
    public LocalP2PNetwork() {
        ThreadManager threadManager = new ThreadManager(1);
        InetSocketAddress multicastAddress = new InetSocketAddress(DISCOVERY_ADDRESS, DISCOVERY_SERVER_PORT);
        IHostDiscovery hostDiscovery = new LocalHostDiscovery(threadManager, multicastAddress);
        IClientDiscovery clientDiscovery = new LocalClientDiscovery(threadManager, multicastAddress);
        client = new P2PClient(threadManager, clientDiscovery);
        host = new P2PHost(threadManager, hostDiscovery);
        LOGGER.info("Network initialized");
    }

    /**
     * Retrieves the host of the local peer-to-peer network.
     *
     * @return The host of the local peer-to-peer network.
     */
    @Override
    public P2PHost getHost() {
        return host;
    }

    /**
     * Retrieves the client of the local peer-to-peer network.
     *
     * @return The client of the local peer-to-peer network.
     */
    @Override
    public P2PClient getClient() {
        return client;
    }

    /**
     * Checks if a network interface is virtual.
     *
     * @param displayName The display name of the network interface.
     * @return True if the network interface is virtual, false otherwise.
     */
    private boolean isVirtualInterface(String displayName) {
        String lowerCaseName = displayName.toLowerCase();
        return lowerCaseName.contains("vmware") || lowerCaseName.contains("virtualbox") || lowerCaseName.contains(
                "vbox") || lowerCaseName.contains("virtual") || lowerCaseName.contains("hyper-v");
    }

    /**
     * Retrieves the server address of the local peer-to-peer network.
     *
     * @return The server address of the local peer-to-peer network.
     */
    @Override
    public InetSocketAddress getServerAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (isInvalidInterface(iface))
                    continue;

                InetAddress addr = getValidInetAddress(iface);
                if (addr != null) {
                    return new InetSocketAddress(addr, DISCOVERY_SERVER_PORT);
                }
            }
        } catch (Exception e) {
            LOGGER.info("Failed to get server address", e);
        }

        return null; // No suitable address found
    }

    /**
     * Checks if a network interface is invalid for use.
     *
     * @param iface The network interface to check.
     * @return True if the interface is invalid, false otherwise.
     */
    private boolean isInvalidInterface(NetworkInterface iface) {
        try {
            return iface.isLoopback() || !iface.isUp() || iface.isVirtual() || isVirtualInterface(iface.getDisplayName());
        } catch (Exception e) {
            LOGGER.info("Error checking network interface: {}", iface.getDisplayName(), e);
            return true;
        }
    }

    /**
     * Retrieves a valid InetAddress from a network interface.
     *
     * @param iface The network interface to retrieve the address from.
     * @return A valid InetAddress, or null if none found.
     */
    private InetAddress getValidInetAddress(NetworkInterface iface) {
        Enumeration<InetAddress> addresses = iface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress addr = addresses.nextElement();
            if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                return addr;
            }
        }
        return null;
    }
}
