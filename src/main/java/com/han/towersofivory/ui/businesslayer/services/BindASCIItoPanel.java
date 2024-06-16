package com.han.towersofivory.ui.businesslayer.services;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

/**
 * Class to bind ASCII art to a panel
 */
public class BindASCIItoPanel {

    private BindASCIItoPanel() {}

    /**
     * Bind ASCII art to a panel
     * @param contentPanel The panel to bind the ASCII art to
     */
    public static void bindASCIItoPanel(Panel contentPanel) {
        String[] towersOfIvoryASCII = {
                "___________________________________________________________________________________________________",
                "___________                                         _____  .___                                    ",
                "\\__    ___/_____  _  __ ___________  ______   _____/ ____\\ |   |__  _____________ ___.__.        ",
                "  |    | /  _ \\ \\/ \\/ // __ \\_  __ \\/  ___/  /  _ \\   __\\  |   \\  \\/ /  _ \\_  __ <   |  |",
                "  |    |(  <_> )     /\\  ___/|  | \\/\\___ \\  (  <_> )  |    |   |\\   (  <_> )  | \\/\\___  |   ",
                "  |____| \\____/ \\/\\_/  \\___  >__|  /____  >  \\____/|__|    |___| \\_/ \\____/|__|   / ____|   ",
                "                           \\/           \\/                                        \\/            ",
                "___________________________________________________________________________________________________"
        };
        for (String s : towersOfIvoryASCII) {
            contentPanel.addComponent(new Label(s));
        }
    }
}
