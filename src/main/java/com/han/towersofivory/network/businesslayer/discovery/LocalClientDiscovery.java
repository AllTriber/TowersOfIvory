package com.han.towersofivory.network.businesslayer.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.PacketSource;
import com.han.towersofivory.network.businesslayer.packets.DiscoveryPacket;
import com.han.towersofivory.network.businesslayer.server.UDPServer;
import com.han.towersofivory.network.businesslayer.threading.ThreadManager;
import com.han.towersofivory.network.dto.HostInformation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * LocalClientDiscovery class that implements the IClientDiscovery interface.
 * It provides methods for discovering hosts, stopping host discovery, getting discovered hosts, and getting the discovery client.
 */
public class LocalClientDiscovery implements IClientDiscovery {
    private final ThreadManager threadManager;
    private final InetSocketAddress multicastAddress;
    private final List<HostInformation> hosts;
    private final Gson gson;
    private UDPServer discoverClient;

    /**
     * Constructor for the LocalClientDiscovery class.
     *
     * @param threadManager    The ThreadManager to manage threads.
     * @param multicastAddress The multicast address for host discovery.
     */
    public LocalClientDiscovery(ThreadManager threadManager, InetSocketAddress multicastAddress) {
        this.hosts = new CopyOnWriteArrayList<>();
        this.multicastAddress = multicastAddress;
        this.threadManager = threadManager;
        this.gson = new GsonBuilder().create();
    }

    /**
     * Discovers hosts at the given host discovery address.
     *
     * @param hostDiscoveryAddress The address to discover hosts at.
     * @throws ConnectionFailedException If the connection fails.
     * @throws SendFailedException       If sending fails.
     */
    @Override
    public void discoverHosts(InetSocketAddress hostDiscoveryAddress) throws ConnectionFailedException, SendFailedException {
        discoverClient = new UDPServer();
        InetSocketAddress clientAddress = new InetSocketAddress(hostDiscoveryAddress.getAddress().getHostAddress(), hostDiscoveryAddress.getPort() + 1);
        discoverClient.start(clientAddress);
        threadManager.runScheduledOperation(discoverClient);

        BasePacket packet = createDiscoveryPacket(clientAddress);
        sendDiscoveryPacket(clientAddress, packet);
    }

    /**
     * Stops the host discovery process.
     */
    @Override
    public void stopHostDiscovery() {
        threadManager.stopOperation(discoverClient);
    }

    /**
     * Gets a list of discovered hosts.
     *
     * @return A list of InetSocketAddresses representing the discovered hosts.
     */
    @Override
    public List<HostInformation> getHosts() {
        return hosts;
    }

    /**
     * Gets the discovery client.
     *
     * @return The UDPServer representing the discovery client.
     */
    public UDPServer getDiscoverClient() {
        return discoverClient;
    }

    /**
     * Creates a discovery packet with the client's IP and port.
     *
     * @param clientAddress The address of the client.
     * @return The created discovery packet.
     */
    private BasePacket createDiscoveryPacket(InetSocketAddress clientAddress) {
        BasePacket packet = new DiscoveryPacket(clientAddress.getAddress().getHostAddress(), clientAddress.getPort());
        packet.setSource(PacketSource.CLIENT);
        return packet;
    }

    /**
     * Sends the discovery packet to the multicast address.
     *
     * @param clientAddress The address of the client.
     * @param packet        The discovery packet to send.
     * @throws SendFailedException If there is an error sending the packet.
     */
    private void sendDiscoveryPacket(InetSocketAddress clientAddress, BasePacket packet) throws SendFailedException {
        try (DatagramSocket socket = new DatagramSocket(0, clientAddress.getAddress())) {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("type", packet.getClass().getName());
            wrapper.add("data", gson.toJsonTree(packet));
            byte[] buffer = gson.toJson(wrapper).getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, multicastAddress);
            socket.send(datagramPacket);
        } catch (IOException e) {
            throw new SendFailedException("Failed to send discovery packet", e);
        }
    }
}
