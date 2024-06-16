package com.han.towersofivory.ui.businesslayer.services.windows.agent;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class AgentWindow extends AbstractWindow {
    private final WindowRouter router;

    public AgentWindow(WindowRouter router) {
        super("Agent Configuration");
        this.router = router;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);
        contentPanel.addComponent(new Label("Agent configurations:"));

        contentPanel.addComponent(new Button("Create New Agent", () -> router.navigateTo("configurationDetailWindow", null, "name")));

        contentPanel.addComponent(new Button("Show my configurations", () -> router.navigateTo("agentConfigurationsWindow")));

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Button("Back", () -> router.navigateTo("homeWindow")));
    }
}
