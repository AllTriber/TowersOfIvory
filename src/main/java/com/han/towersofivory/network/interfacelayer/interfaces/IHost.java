package com.han.towersofivory.network.interfacelayer.interfaces;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.dto.HostInformation;

import java.util.UUID;

/**
 * Interface for the network.
 *
 * @author Roward Dorrestijn
 * @version 1.0
 * @since 1.0
 */
public interface IHost {
    /**
     * Sends an action.
     *
     * @param packet the action to send
     * @see BasePacket
     */
    void sendPacketToClients(BasePacket packet);

    /**
     * Sends an action to a specific client.
     *
     * @param uuid   the UUID of the client
     * @param packet the action to send
     * @see BasePacket
     */
    void sendPacketToClient(UUID uuid, BasePacket packet);

    /**
     * Updates the host information.
     *
     * @param gameInfo the game information
     */
    void updateHostInformation(GameInfo gameInfo);

    /**
     * Starts the host.
     *
     * @param gameInfo the game information
     * @return the host information
     * @throws ConnectionFailedException if the connection fails
     */
    HostInformation startHost(GameInfo gameInfo) throws ConnectionFailedException;

    /**
     * Stops the host.
     */
    void stopHost();

    /**
     * Registers a packet listener.
     *
     * @param listener the packet listener
     * @see IPacketListener
     */
    void registerHostListener(IPacketListener listener);
}