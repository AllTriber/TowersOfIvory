package com.han.towersofivory.agent.interfacelayer.mappers;

import com.han.towersofivory.agent.businesslayer.services.AgentService;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.exceptions.AgentCompilerException;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;

import java.io.File;

public class ConcreteAgent implements IAgent {

    private final AgentService agentService;

    public ConcreteAgent() {
        this.agentService = new AgentService("src/main/resources/agentConfigurations/");
    }

    String noSupport = "Not supported yet.";

    @Override
    public void saveConfiguration(AgentConfigurationText agentConfigurationText) {
        agentService.saveConfiguration(agentConfigurationText);
    }

    @Override
    public File[] loadAllConfigurations() {
        return agentService.loadAllConfigurations();
    }

    @Override
    public AgentConfigurationText loadConfiguration(AgentConfigurationText agentConfigurationText) {
        throw new UnsupportedOperationException(noSupport);
    }

    @Override
    public void checkConfiguration(AgentConfigurationText agentConfigurationText) throws AgentCompilerException {
        agentService.checkConfiguration(agentConfigurationText);
    }

    @Override
    public String loadAgent() {
        throw new UnsupportedOperationException(noSupport);
    }

    @Override
    public void deleteConfiguration(String fileName) {
        agentService.deleteConfiguration(fileName);
    }

    @Override
    public AgentConfigurationText getAgent(String agentConfig) {
        return agentService.getAgentConfiguration(agentConfig);
    }
}
