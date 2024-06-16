package com.han.towersofivory.ui.businesslayer.services.windows.hostgame;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class CreateOrLoadWindow extends AbstractWindow {
    private final WindowRouter router;
    public CreateOrLoadWindow(WindowRouter router) {
        super("Host Game");
        this.router = router;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);
        contentPanel.addComponent(new Label("Host game:"));

        contentPanel.addComponent(new Button("Create new game", () -> router.navigateTo("createNewGameWindow")));

        contentPanel.addComponent(new Button("Resume old game", () -> router.navigateTo("loadOldGameWindow")));

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Button("Back", () -> router.navigateTo("homeWindow")));
    }
}
