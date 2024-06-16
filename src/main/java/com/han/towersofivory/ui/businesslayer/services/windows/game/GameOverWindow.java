package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

/**
 * The game window
 */
public class GameOverWindow extends AbstractWindow {

    private final WindowRouter router;

    private final String message;


    public GameOverWindow(WindowRouter router, String message, String title) {
        super(title);
        this.router = router;
        this.message = message;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        contentPanel.setLayoutManager(new BorderLayout());
        contentPanel.addComponent(new Label(message));
        Button homeButton = new Button("Exit game", this::returnHome);
        contentPanel.addComponent(homeButton);
        homeButton.takeFocus();
    }

    private void returnHome() {
      System.exit(0);
    }

}
