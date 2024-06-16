package com.han.towersofivory.game.businesslayer.gamecontroller.listeners.host;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.gamecontroller.GameController;
import com.han.towersofivory.game.businesslayer.packets.ActionPacket;
import com.han.towersofivory.game.businesslayer.packets.AgentConfigurationPacket;
import com.han.towersofivory.game.businesslayer.packets.GameStatePacket;
import com.han.towersofivory.network.businesslayer.packet.IPacketHandler;
import com.han.towersofivory.network.businesslayer.packet.IPacketListener;
import com.han.towersofivory.network.businesslayer.packet.PacketPriority;
import com.han.towersofivory.network.businesslayer.packets.ConnectionPacket;
import com.han.towersofivory.network.businesslayer.packets.DisconnectPacket;
import com.han.towersofivory.network.interfacelayer.interfaces.IHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerPacketListeners implements IPacketListener {
    private static final Logger LOGGER = LogManager.getLogger(ServerPacketListeners.class);
    private final GameController game;
    private final IHost network;

    public ServerPacketListeners(GameController game, IHost network) {
        this.game = game;
        this.network = network;
    }

    @IPacketHandler
    public void packetHandler(ActionPacket actionPacket) {
        LOGGER.info("Host received action packet");
        game.handleActionOnHost(actionPacket.getPlayerUUID(), actionPacket.getAction(), actionPacket.getArgs());
    }

    @IPacketHandler(priority = PacketPriority.LOW)
    public void onClientConnection(ConnectionPacket connectionPacket) {
        LOGGER.info("Host received connection packet, updating host information");

        GameInfo gameInfo = new GameInfo(game.getGameConfiguration(), game.getWorld().getNumberOfPlayers());
        network.updateHostInformation(gameInfo);
        
        game.updateGameStateBeforeJoin();
        network.sendPacketToClients(new GameStatePacket(game.getGameState()));
    }

    @IPacketHandler (priority = PacketPriority.LOWEST)
    public void packetHandler(AgentConfigurationPacket agentConfigurationPacket) {
        LOGGER.info("Host received agent configuration packet from {}", agentConfigurationPacket.getTargetUUID());
        network.sendPacketToClients(agentConfigurationPacket);
    }

    @IPacketHandler
    public void packetHandler(DisconnectPacket disconnectPacket) {
        GameInfo gameInfo = new GameInfo(game.getGameConfiguration(), game.getWorld().getNumberOfPlayers());
        network.updateHostInformation(gameInfo);
    }
}

