package com.han.towersofivory.ui.businesslayer.services.windows.joingame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.network.dto.HostInformation;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import com.han.towersofivory.ui.exceptions.UIUpdateException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class LoadHostsWindow extends AbstractWindow {
    private final WindowRouter router;
    private final GameMapper game;
    private final Long expectedLoadingTimeMillis;


    public LoadHostsWindow(WindowRouter router, GameMapper game, Long expectedLoadingTimeMillis) {
        super("Loading...");
        this.router = router;
        this.game = game;
        this.expectedLoadingTimeMillis = expectedLoadingTimeMillis;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        contentPanel.setLayoutManager(new GridLayout(1));

        Panel loadingPanel = new Panel(new GridLayout(1));
        loadingPanel.addComponent(new EmptySpace(new TerminalSize(1, 2)));

        // loading message
        Panel messagePanel = new Panel(new GridLayout(2));
        Label loadingLabel = new Label("Loading host list, please wait... ");
        messagePanel.addComponent(loadingLabel);

        // spinner
        Label spinnerLabel = new Label("|");
        messagePanel.addComponent(spinnerLabel);
        loadingPanel.addComponent(messagePanel);

        // loading bar
        Panel loadingBarPanel = new Panel(new GridLayout(20));
        Label[] loadingBlocks = new Label[20];
        for (int i = 0; i < 20; i++) {
            loadingBlocks[i] = new Label(" ");
            loadingBlocks[i].setBackgroundColor(TextColor.ANSI.BLACK);
            loadingBarPanel.addComponent(loadingBlocks[i]);
        }
        loadingPanel.addComponent(loadingBarPanel);
        loadingPanel.addComponent(new EmptySpace(new TerminalSize(1, 2)));

        contentPanel.addComponent(loadingPanel);

        CompletableFuture<List<HostInformation>> availableHostsFuture = CompletableFuture.supplyAsync(game::getHosts);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            private final String[] spinnerChars = {"|", "/", "-", "\\"};
            private int spinnerIndex = 0;
            private int progress = 0;

            @Override
            public void run() {
                try {
                    // Update spinner and loading bar
                    getTextGUI().getGUIThread().invokeLater(() -> {
                        spinnerLabel.setText(spinnerChars[spinnerIndex]);
                        spinnerIndex = (spinnerIndex + 1) % spinnerChars.length;

                        if (progress < 20) {
                            loadingBlocks[progress].setBackgroundColor(TextColor.ANSI.BLUE);
                            progress++;
                        } else {
                            availableHostsFuture.whenComplete((availableHosts, throwable) -> {
                                close();
                                timer.cancel();
                                router.navigateTo("joinGameWindow", availableHosts);
                            });
                        }
                    });
                } catch (Exception e) {
                    throw new UIUpdateException("Error occurred while updating UI", e);
                }
            }
        }, 0, expectedLoadingTimeMillis / 20);
    }
}
