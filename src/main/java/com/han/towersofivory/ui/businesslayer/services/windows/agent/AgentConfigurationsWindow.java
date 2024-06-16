package com.han.towersofivory.ui.businesslayer.services.windows.agent;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class AgentConfigurationsWindow extends AbstractWindow {
    private final WindowRouter router;
    private final IAgent agent = new ConcreteAgent();

    public AgentConfigurationsWindow(WindowRouter router) {
        super("Agent Configurations");
        this.router = router;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);
        contentPanel.addComponent(new Label("Agent configurations:"));

        List<File> configFiles = new ArrayList<>();
        File[] files = agent.loadAllConfigurations();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                configFiles.add(file);
            }
        }

        for (File file : configFiles) {
            String title = file.getName().replace(".txt", "");
            contentPanel.addComponent(new Button(title, () -> {
                close();
                router.navigateTo("configurationDetailWindow", file, title);
            }));
        }

        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Button("Back", () -> {
            close();
            router.navigateTo("agentWindow");
        }));
    }
}
