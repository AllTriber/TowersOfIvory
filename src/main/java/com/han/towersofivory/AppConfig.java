package com.han.towersofivory;

import com.han.towersofivory.game.interfacelayer.mappers.GameMapper;
import com.han.towersofivory.ui.businesslayer.services.ASCIIWorldService;
import com.han.towersofivory.ui.businesslayer.services.UIService;
import com.han.towersofivory.ui.businesslayer.services.factory.DefaultWindowFactory;
import com.han.towersofivory.ui.businesslayer.services.factory.IWindowFactory;
import com.han.towersofivory.ui.businesslayer.services.windows.WindowConfiguration;
import com.han.towersofivory.ui.interfacelayer.mappers.UIMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Spring configuration class for the application
 */
@Configuration
public class AppConfig {

    @Bean
    public UIService uiService(WindowConfiguration windowConfiguration) {
        return new UIService(windowConfiguration);
    }

    /**
     * Bean for UIMapper
     *
     * @param uiService This is a lazy bean to avoid circular dependencies
     * @return UIMapper
     */
    @Bean
    public UIMapper uiMapper(@Lazy UIService uiService) {
        return new UIMapper(uiService);
    }

    @Bean
    public GameMapper gameMapper(UIMapper uiMapper) {
        return new GameMapper(uiMapper);
    }

    /**
     * Bean for IWindowFactory
     *
     * @param context ApplicationContext
     * @param uiService This is a lazy bean to avoid circular dependencies
     * @return IWindowFactory
     */
    @Bean
    public IWindowFactory windowFactory(ApplicationContext context, @Lazy UIService uiService) {
        return new DefaultWindowFactory(context, uiService);
    }

    @Bean
    public WindowConfiguration windowConfiguration(IWindowFactory windowFactory) {
        return new WindowConfiguration(windowFactory);
    }

    @Bean
    public ASCIIWorldService aSCIIWorldService() {
        return new ASCIIWorldService();
    }
}
