package com.han.towersofivory.agent.dto;

public class AgentConfigurationText {
    private String title;
    private String configuration;


    public AgentConfigurationText(String title, String configuration) {
        this.title = title;
        this.configuration = configuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
