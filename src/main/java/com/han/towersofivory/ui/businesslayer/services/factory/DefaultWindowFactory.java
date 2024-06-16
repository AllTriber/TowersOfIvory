package com.han.towersofivory.ui.businesslayer.services.factory;

import com.googlecode.lanterna.gui2.Window;
import com.han.towersofivory.ui.businesslayer.services.UIService;
import com.han.towersofivory.ui.businesslayer.services.observer.GameWorldObserver;
import com.han.towersofivory.ui.businesslayer.services.windows.WindowConfiguration;
import com.han.towersofivory.ui.exceptions.WindowCreationException;
import org.springframework.context.ApplicationContext;

public class DefaultWindowFactory implements IWindowFactory {
    private final ApplicationContext context;
    private final UIService uiService;

    public DefaultWindowFactory(ApplicationContext context, UIService uiService) {
        this.context = context;
        this.uiService = uiService;
    }
    /**
     * Create a window with the given ID and parameters.
     *
     * @param windowId The ID of the window to create.
     * @param args The parameters to pass to the window.
     * @return The created window.
     */
    @Override
    public Window createWindow(String windowId, Object... args) {
        WindowConfiguration.WindowInfo info = getWindowInfo(windowId);
        validateWindowInfo(windowId, info);

        Object[] params = buildParameters(info, args);
        return instantiateWindow(info, params, windowId);
    }

    private WindowConfiguration.WindowInfo getWindowInfo(String windowId) {
        return WindowConfiguration.getWindowInfo(windowId);
    }

    private void validateWindowInfo(String windowId, WindowConfiguration.WindowInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("No window configuration found for id: " + windowId);
        }
    }

    private Object[] buildParameters(WindowConfiguration.WindowInfo info, Object[] args) {
        Object[] params = new Object[info.parameterTypes().size()];
        int argsIndex = 0;

        for (int i = 0; i < params.length; i++) {
            params[i] = getParameter(info, args, argsIndex, i);
            if (argsIndex < args.length && params[i] == args[argsIndex]) {
                argsIndex++;
            }
        }

        return params;
    }

    private Object getParameter(WindowConfiguration.WindowInfo info, Object[] args, int argsIndex, int paramIndex) {
        Class<?> paramType = info.parameterTypes().get(paramIndex);

        if (argsIndex < args.length && args[argsIndex] == null) {
            return null;
        } else if (argsIndex < args.length && paramType.isInstance(args[argsIndex])) {
            return args[argsIndex];
        } else {
            return getBeanForParameter(paramType);
        }
    }

    private Object getBeanForParameter(Class<?> paramType) {
        String simpleName = paramType.getSimpleName();
        String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);

        if (context.containsBean(beanName)) {
            return context.getBean(beanName);
        } else {
            throw new IllegalArgumentException("No argument provided for parameter: " + paramType.getName());
        }
    }

    private Window instantiateWindow(WindowConfiguration.WindowInfo info, Object[] params, String windowId) {
        try {
            Window window = info.windowClass()
                    .getConstructor(info.parameterTypes().toArray(new Class[0]))
                    .newInstance(params);

            // Currently only GameWindow is an observer (and will be for the foreseeable future). So this is hardcoded.
            if (Boolean.TRUE.equals(info.isObserver())) {
                uiService.addObserver((GameWorldObserver) window);
            }

            return window;
        } catch (Exception e) {
            throw new WindowCreationException("Failed to create window for id: " + windowId, e);
        }
    }
}
