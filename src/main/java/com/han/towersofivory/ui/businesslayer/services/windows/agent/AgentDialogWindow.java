package com.han.towersofivory.ui.businesslayer.services.windows.agent;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;

public class AgentDialogWindow extends AbstractWindow {
    private final WindowRouter router;
    private final String message;
    private final DialogType type;
    private final Runnable onConfirm;

    public AgentDialogWindow(WindowRouter router, String title, String message, DialogType type, Runnable onConfirm) {
        super(title);
        this.router = router;
        this.message = message;
        this.type = type;
        this.onConfirm = onConfirm;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        contentPanel.addComponent(new Label(message));

        contentPanel.addComponent(new Button("Ok", this::okRunnable));

        if(type == DialogType.WARNING) {
            contentPanel.addComponent(new Button("Cancel", this::close));
            contentPanel.addComponent(new Button("I'm feeling lucky", () -> {
                if(System.currentTimeMillis() % 2 == 0) {
                    okRunnable();
                } else {
                    close();
                }
            }));
        }
    }

    private void okRunnable() {
        close();
        if (onConfirm != null) {
            onConfirm.run();
        }
    }
}
