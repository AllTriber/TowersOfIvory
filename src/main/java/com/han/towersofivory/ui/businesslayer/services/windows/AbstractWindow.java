package com.han.towersofivory.ui.businesslayer.services.windows;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractWindow extends BasicWindow {
    protected final Panel contentPanel;

    protected AbstractWindow(String title) {
        super(title);
        this.contentPanel = new Panel();
        Collection<Hint> hints = new ArrayList<>();
        hints.add(Hint.CENTERED);
        this.setHints(hints);
        setComponent(contentPanel);
    }

    protected abstract void initializeComponents();
}
