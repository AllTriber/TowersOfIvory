package com.han.towersofivory.ui.businesslayer.services.windows.hostgame;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

public class ShowErrorWindow extends AbstractWindow {
    private final String message;

    public ShowErrorWindow(WindowRouter router, String title, String message) {
        super(title);
        this.message = message;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        contentPanel.addComponent(new Label(message));
        contentPanel.addComponent(new Button("OK", this::close));
    }
}
