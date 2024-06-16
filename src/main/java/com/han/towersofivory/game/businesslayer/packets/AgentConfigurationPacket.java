package com.han.towersofivory.game.businesslayer.packets;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;

import java.util.UUID;

public class AgentConfigurationPacket extends BasePacket {

    private final AgentConfigurationText agentConfig;
    private UUID targetUUID;

    public AgentConfigurationPacket(AgentConfigurationText agentConfig , UUID targetUUID) {
        this.agentConfig = agentConfig;
        this.targetUUID = targetUUID;
    }
    public AgentConfigurationText getAgentConfig() {
        return agentConfig;
    }
    public UUID getTargetUUID() {
        return targetUUID;
    }
    public void setTargetUUID(UUID targetUUID) {
        this.targetUUID = targetUUID;
    }
}
