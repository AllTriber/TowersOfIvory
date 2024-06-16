package com.han.towersofivory.ui.businesslayer.services;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.network.businesslayer.packet.BasePacket;
import com.han.towersofivory.ui.businesslayer.services.observer.GameWorldObserver;
import com.han.towersofivory.ui.businesslayer.services.windows.WindowConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIService {
    private static final Logger LOGGER = LogManager.getLogger(UIService.class);
    private static final String TITLE = "Towers of Ivory";
    private WindowRouter router;
    private final WindowConfiguration windowConfiguration;
    private final List<GameWorldObserver> observers = new ArrayList<>();

    @Autowired
    public UIService(WindowConfiguration windowConfiguration) {
        this.windowConfiguration = windowConfiguration;
    }

    public void showTUI() {
        try (SwingTerminalFrame swingTerminalFrame = createTerminalFrame()) {
            configureTerminalFrame(swingTerminalFrame);
            Screen screen = createAndStartScreen(swingTerminalFrame);
            initializeGUI(screen, swingTerminalFrame);
            router.navigateTo("homeWindow");
            screen.stopScreen();
        } catch (IOException e) {
            LOGGER.error("Failed to start TUI", e);
        }
    }

    private SwingTerminalFrame createTerminalFrame() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(128, 72));
        terminalFactory.setTerminalEmulatorTitle(TITLE);
        terminalFactory.setTerminalEmulatorFontConfiguration(getCustomFontConfiguration());
        terminalFactory.setForceAWTOverSwing(true);
        terminalFactory.setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);
        return terminalFactory.createSwingTerminal();
    }

    private SwingTerminalFontConfiguration getCustomFontConfiguration() {
        Font customFont = new Font("Monospaced", Font.PLAIN, 20);
        return SwingTerminalFontConfiguration.newInstance(customFont);
    }

    private void configureTerminalFrame(SwingTerminalFrame swingTerminalFrame) throws IOException {
        swingTerminalFrame.setVisible(true);
        swingTerminalFrame.setSize(1280, 720);
        swingTerminalFrame.setLocationRelativeTo(null);
        setGameLogo(swingTerminalFrame);
    }

    private void setGameLogo(SwingTerminalFrame swingTerminalFrame) throws IOException {
        File logoImage = new File("src/main/resources/Logo.jpg");
        BufferedImage logo = ImageIO.read(logoImage);
        swingTerminalFrame.setIconImage(logo);
    }

    private Screen createAndStartScreen(SwingTerminalFrame swingTerminalFrame) throws IOException {
        Screen screen = new TerminalScreen(swingTerminalFrame);
        screen.startScreen();
        return screen;
    }

    private void initializeGUI(Screen screen, SwingTerminalFrame terminalFrame) {
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK_BRIGHT));
        router = new WindowRouter(gui, windowConfiguration, terminalFrame);
    }

    public void updateUI(World world, BasePacket packet) {
        for (GameWorldObserver observer : observers) {
            observer.updateUI(world, packet);
        }
    }

    public void addObserver(GameWorldObserver observer) {
        observers.add(observer);
    }

}
