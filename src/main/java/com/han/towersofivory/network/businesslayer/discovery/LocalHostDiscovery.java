package com.han.towersofivory.network.businesslayer.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.exceptions.SendFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.PacketSource;
import com.han.towersofivory.network.businesslayer.packets.DiscoveryPacket;
import com.han.towersofivory.network.businesslayer.server.MulticastServer;
import com.han.towersofivory.network.businesslayer.threading.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * LocalHostDiscovery class that implements the IHostDiscovery interface.
 * It provides methods for allowing host discovery, disabling host discovery, broadcasting presence, and getting the host discovery server.
 */
public class LocalHostDiscovery implements IHostDiscovery {
    private static final Logger LOGGER = LogManager.getLogger(LocalHostDiscovery.class);
    private final ThreadManager threadManager;
    private final InetSocketAddress multicastAddress;
    private final Gson gson;
    private MulticastServer hostDiscoveryServer;
    private GameInfo gameInfo;

    /**
     * Constructor for the LocalHostDiscovery class.
     *
     * @param threadManager    The ThreadManager to manage threads.
     * @param multicastAddress The multicast address for host discovery.
     */
    public LocalHostDiscovery(ThreadManager threadManager, InetSocketAddress multicastAddress) {
        this.multicastAddress = multicastAddress;
        this.threadManager = threadManager;
        this.gson = new GsonBuilder().create();
    }

    /**
     * Allows host discovery at the given host discovery address.
     *
     * @param hostDiscoveryAddress The address to allow host discovery at.
     * @throws ConnectionFailedException If the connection fails.
     */
    @Override
    public void allowHostDiscovery(InetSocketAddress hostDiscoveryAddress, GameInfo gameInfo) throws ConnectionFailedException {
        this.gameInfo = gameInfo;
        hostDiscoveryServer = new MulticastServer(multicastAddress);
        hostDiscoveryServer.start(hostDiscoveryAddress);
        threadManager.runScheduledOperation(hostDiscoveryServer);
    }

    /**
     * Updates the game information.
     *
     * @param gameInfo The game information to be used for host discovery.
     */
    @Override
    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    /**
     * Disables the host discovery process.
     */
    @Override
    public void disableHostDiscovery() {
        threadManager.stopOperation(hostDiscoveryServer);
    }

    /**
     * Broadcasts the presence of the host to a target machine.
     *
     * @param targetMachineAddress The address of the target machine to broadcast presence to.
     */
    @Override
    public void broadcastPresence(InetSocketAddress targetMachineAddress) {
        try {
            BasePacket packet = createDiscoveryPacket();
            sendDiscoveryPacket(targetMachineAddress, packet);
        } catch (SendFailedException ignored) {
            LOGGER.info("Failed to broadcast presence");
        }
    }

    /**
     * Creates a discovery packet with the host's IP and port.
     *
     * @return The created discovery packet.
     */
    private BasePacket createDiscoveryPacket() {
        BasePacket packet = new DiscoveryPacket(
                hostDiscoveryServer.getIp(),
                hostDiscoveryServer.getPort(),
                gameInfo
        );
        packet.setSource(PacketSource.SERVER);
        return packet;
    }

    /**
     * Sends the discovery packet to the target machine address.
     *
     * @param targetMachineAddress The address of the target machine to send the packet to.
     * @param packet               The discovery packet to send.
     * @throws SendFailedException If there is an error sending the packet.
     */
    private void sendDiscoveryPacket(InetSocketAddress targetMachineAddress, BasePacket packet) throws SendFailedException {
        try (DatagramSocket socket = new DatagramSocket()) {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("type", packet.getClass().getName());
            wrapper.add("data", gson.toJsonTree(packet));
            byte[] buffer = gson.toJson(wrapper).getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, targetMachineAddress);
            socket.send(datagramPacket);
        } catch (IOException e) {
            throw new SendFailedException("Failed to send discovery packet", e);
        }
    }

    /**
     * Gets the host discovery server.
     *
     * @return The MulticastServer representing the host discovery server.
     */
    public MulticastServer getHostDiscoveryServer() {
        return hostDiscoveryServer;
    }
}
