package com.han.towersofivory;

import com.han.towersofivory.ui.businesslayer.services.UIService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UIService uiService = appContext.getBean(UIService.class);
        uiService.showTUI();

    }
}