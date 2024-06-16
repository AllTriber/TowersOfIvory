package com.han.towersofivory.network.businesslayer.server;

import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

/**
 * MulticastServer is a class that extends the BaseUDPServer class.
 * It represents a server that can join a multicast group and send/receive packets to/from that group.
 * It contains methods to start and stop the server, and to join and leave the multicast group.
 */
public class MulticastServer extends BaseUDPServer {
    private final InetSocketAddress multicastAddress;
    private MembershipKey membershipKey;

    /**
     * Constructor for the MulticastServer class.
     *
     * @param multicastAddress The multicast group address to join.
     */
    public MulticastServer(InetSocketAddress multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    /**
     * Starts the server with the given host address and joins the multicast group.
     *
     * @param hostAddress The address of the host.
     * @throws ConnectionFailedException If the server fails to start or join the multicast group.
     */
    @Override
    public void start(InetSocketAddress hostAddress) throws ConnectionFailedException {
        try {
            internalStart(hostAddress);
            NetworkInterface networkInterface = getNetworkInterface(hostAddress);
            membershipKey = joinMulticastGroup(datagramChannel, multicastAddress, networkInterface);
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed to join multicast group", e);
        }
    }

    /**
     * Stops the server and leaves the multicast group.
     */
    @Override
    public void stop() {
        super.stop();

        if (membershipKey != null) {
            membershipKey.drop();
            membershipKey = null;
        }
    }

    /**
     * Retrieves the network interface for the given host address.
     *
     * @param hostAddress The host address.
     * @return The network interface.
     * @throws IOException If an I/O error occurs.
     */
    protected NetworkInterface getNetworkInterface(InetSocketAddress hostAddress) throws IOException {
        return NetworkInterface.getByInetAddress(hostAddress.getAddress());
    }

    /**
     * Joins the multicast group on the given channel and network interface.
     *
     * @param channel          The datagram channel.
     * @param multicastAddress The multicast group address.
     * @param networkInterface The network interface.
     * @return The membership key.
     * @throws IOException If an I/O error occurs.
     */
    protected MembershipKey joinMulticastGroup(DatagramChannel channel, InetSocketAddress multicastAddress, NetworkInterface networkInterface) throws IOException {
        return channel.join(multicastAddress.getAddress(), networkInterface);
    }
}
