package com.han.towersofivory.network.interfacelayer.interfaces;

import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.dto.HostInformation;

import java.util.List;
import java.util.UUID;

/**
 * Interface for handling the connection.
 *
 * @author Roward Dorrestijn, Pepijn van den Ende
 * @version 1.0
 * @since 1.0
 */
public interface IClient {

    /**
     * Finds hosts.
     *
     * @return The hosts found.
     * @see HostInformation
     */
    List<HostInformation> findHosts();

    /**
     *
     * UC4 Deelnemen Spel
     *
     * Connects to a host.
     *
     * @param host The host to connect to.
     * @see HostInformation
     */
    void connectToHost(HostInformation host) throws ConnectionFailedException;

    /**
     * Disconnects from a host.
     *
     * @param playerId The player id to disconnect.
     * @see UUID
     */
    void disconnect(UUID playerId);

    void sendPacketToHost(BasePacket packet);

    void registerClientListener(IPacketListener listener);
}
