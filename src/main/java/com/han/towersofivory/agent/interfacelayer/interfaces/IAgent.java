package com.han.towersofivory.agent.interfacelayer.interfaces;

import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.exceptions.AgentCompilerException;

import java.io.File;

/**
 * Interface for the agent.
 *
 * @author Roward Dorrestijn, Pepijn van den Ende
 * @version 2.0
 * @since 1.0
 */
public interface IAgent {
    void saveConfiguration(AgentConfigurationText agentConfigurationText);

    void checkConfiguration(AgentConfigurationText agentConfigurationText) throws AgentCompilerException;

    File[] loadAllConfigurations();

    AgentConfigurationText loadConfiguration(AgentConfigurationText agentConfigurationText);

    String loadAgent();

    void deleteConfiguration(String fileName);

    AgentConfigurationText getAgent(String agentConfig);
}
