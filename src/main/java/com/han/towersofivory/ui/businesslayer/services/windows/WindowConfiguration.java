package com.han.towersofivory.ui.businesslayer.services.windows;

import com.googlecode.lanterna.gui2.Window;
import com.han.towersofivory.game.businesslayer.worldgeneration.world.World;
import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.businesslayer.services.ASCIIWorldService;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.factory.IWindowFactory;
import com.han.towersofivory.ui.businesslayer.services.windows.agent.*;
import com.han.towersofivory.ui.businesslayer.services.windows.game.ChatWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.game.GameOverWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.game.GameWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.home.HomeWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.hostgame.CreateNewGameWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.hostgame.CreateOrLoadWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.hostgame.LoadOldGameWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.hostgame.ShowErrorWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.joingame.JoinGameWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.joingame.LoadHostsWindow;
import com.han.towersofivory.ui.businesslayer.services.windows.leavegame.LeaveGameWindow;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowConfiguration {
    private static final Map<String, WindowInfo> windowConfig = new HashMap<>();
    private final IWindowFactory windowFactory;

    public WindowConfiguration(IWindowFactory windowFactory) {
        this.windowFactory = windowFactory;
        initializeWindowConfig();
    }

    /**
     * Initialize the window configuration.
     * Register all windows with their expected parameters.
     * The {@link WindowRouter} is always expected as the first parameter.
     * If a parameter is not provided when creating a window, the window will try to find a bean of the expected type.
     * If no bean is found, an exception will be thrown.
     */
    private void initializeWindowConfig() {
        //** | HOME | **//
        windowConfig.put("homeWindow", new WindowInfo(HomeWindow.class, List.of(WindowRouter.class), false));

        //** | AGENT | **//
        windowConfig.put("agentWindow", new WindowInfo(AgentWindow.class, List.of(WindowRouter.class), false));
        windowConfig.put("agentConfigurationsWindow", new WindowInfo(AgentConfigurationsWindow.class, List.of(WindowRouter.class), false));
        windowConfig.put("configurationDetailWindow", new WindowInfo(ConfigurationDetailWindow.class, List.of(WindowRouter.class,
                File.class, // agent configuration file
                String.class // title
        ), false));
        windowConfig.put("agentDialogWindow", new WindowInfo(AgentDialogWindow.class, List.of(WindowRouter.class,
                String.class, // title
                String.class, // message
                DialogType.class, // type
                Runnable.class // onConfirm
        ), false));

        //** | JoinGame | **//
        windowConfig.put("joinGameWindow", new WindowInfo(JoinGameWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                List.class // availableHosts
        ), false));
        windowConfig.put("loadHostsWindow", new WindowInfo(LoadHostsWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                Long.class // expectedLoadingTimeMillis
        ), false));

        //** | HostGame | **//
        windowConfig.put("createOrLoadWindow", new WindowInfo(CreateOrLoadWindow.class, List.of(WindowRouter.class), false));

        windowConfig.put("loadOldGameWindow", new WindowInfo(LoadOldGameWindow.class, List.of(WindowRouter.class, GameMapper.class), false));

        windowConfig.put("createNewGameWindow", new WindowInfo(CreateNewGameWindow.class, List.of(WindowRouter.class,
                GameMapper.class // game
        ), false));
        windowConfig.put("showErrorWindow", new WindowInfo(ShowErrorWindow.class, List.of(WindowRouter.class,
                String.class, // title
                String.class // message
        ), false));

        //** | Game | **//
        windowConfig.put("gameWindowWithoutWorld", new WindowInfo(GameWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                ASCIIWorldService.class // worldService
        ), true));
        windowConfig.put("gameWindowWithWorld", new WindowInfo(GameWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                ASCIIWorldService.class, // worldService
                World.class // world
        ), true));
        windowConfig.put("gameOverWindow", new WindowInfo(GameOverWindow.class, List.of(WindowRouter.class,
                String.class, // message
                String.class // title
        ), false));
        windowConfig.put("ChatWindow", new WindowInfo(ChatWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                Runnable.class, // onClose
                String.class // inGameName
        ), false));

        //** | LeaveGame | **//
        windowConfig.put("leaveGameWindow", new WindowInfo(LeaveGameWindow.class, List.of(WindowRouter.class,
                GameMapper.class, // game
                Runnable.class //onClose
        ), false));

    }

    public static WindowInfo getWindowInfo(String windowId) {
        return windowConfig.get(windowId);
    }

    public record WindowInfo(Class<? extends Window> windowClass, List<Class<?>> parameterTypes, Boolean isObserver) {
    }

    /**
     * Create a window with the given ID and parameters.
     *
     * @param windowId The ID of the window to create.
     * @param params The parameters to pass to the window.
     * @return The created window.
     */
    public Window createWindow(String windowId, Object... params) {
        return windowFactory.createWindow(windowId, params);
    }
}