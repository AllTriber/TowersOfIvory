package com.han.towersofivory.network.interfacelayer.mappers;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.network.businesslayer.IP2PNetwork;
import com.han.towersofivory.network.businesslayer.LocalP2PNetwork;
import com.han.towersofivory.network.businesslayer.exceptions.ConnectionFailedException;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.network.interfacelayer.interfaces.IClient;
import com.han.towersofivory.network.interfacelayer.interfaces.IHost;

import java.util.List;
import java.util.UUID;

/**
 * Mapper voor het netwerk.
 */
public class NetworkMapper implements IClient, IHost {
    private final IP2PNetwork network;

    /**
     * Constructor voor NetworkMapper.
     */
    public NetworkMapper() {
        this.network = new LocalP2PNetwork();
    }

    /**
     * Vindt hosts.
     *
     * @return De gevonden hosts.
     * @see HostInformation
     */
    @Override
    public List<HostInformation> findHosts() {
        network.getClient().discoverHosts(network.getServerAddress(), 300);
        return network.getClient().getHosts().stream().toList();
    }

    /**
     * UC4 Deelnemen Spel
     *
     * Verbindt met een host.
     *
     * @param host De host waarmee verbonden moet worden.
     * @throws ConnectionFailedException Als de verbinding mislukt.
     */
    @Override
    public void connectToHost(HostInformation host) throws ConnectionFailedException {
        network.getClient().connectToHost(host.getAddress());
    }

    /**
     * Verbreekt de verbinding.
     *
     * @param playerId De ID van de speler.
     */
    @Override
    public void disconnect(UUID playerId) {
        network.getClient().disconnectFromHost();
    }

    /**
     * Verzendt een pakket naar de host.
     *
     * @param packet Het te verzenden pakket.
     */
    @Override
    public void sendPacketToHost(BasePacket packet) {
        network.getClient().sendPacket(packet);
    }

    /**
     * Registreert een client listener.
     *
     * @param listener De listener om te registreren.
     */
    @Override
    public void registerClientListener(IPacketListener listener) {
        network.getClient().registerPacketListener(listener);
    }

    /**
     * Verzendt een pakket naar de client.
     *
     * @param packet Het te verzenden pakket.
     */
    @Override
    public void sendPacketToClients(BasePacket packet) {
        network.getHost().broadcastPacket(packet);
    }

    /**
     * Verzendt een pakket naar de client.
     *
     * @param uuid   De UUID van de client.
     * @param packet Het te verzenden pakket.
     */
    @Override
    public void sendPacketToClient(UUID uuid, BasePacket packet) {
        network.getHost().sendPacketToClient(uuid, packet);
    }

    /**
     * Start een host.
     *
     * @return Informatie over de gestarte host.
     * @throws ConnectionFailedException Als het starten van de host mislukt.
     */
    @Override
    public HostInformation startHost(GameInfo gameInfo) throws ConnectionFailedException {
        network.getHost().startServer(network.getServerAddress(), gameInfo);
        return new HostInformation(network.getServerAddress(), gameInfo);
    }

    /**
     * Update de host informatie.
     *
     * @param gameInfo De nieuwe game informatie.
     */
    @Override
    public void updateHostInformation(GameInfo gameInfo) {
        network.getHost().updateGameInfo(gameInfo);
    }

    /**
     * Stopt de host.
     */
    @Override
    public void stopHost() {
        network.getHost().stopServer();
    }

    /**
     * Registreert een host listener.
     *
     * @param listener De listener om te registreren.
     */
    @Override
    public void registerHostListener(IPacketListener listener) {
        network.getHost().registerPacketListener(listener);
    }
}