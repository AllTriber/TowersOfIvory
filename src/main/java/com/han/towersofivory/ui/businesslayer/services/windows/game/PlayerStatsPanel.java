package com.han.towersofivory.ui.businesslayer.services.windows.game;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

import java.util.Map;

/**
 * Panel to display player stats
 */
public class PlayerStatsPanel extends Panel {

    public PlayerStatsPanel() {
        super();
    }

    /**
     * Update the player stats in this panel
     *
     * @param stats the stats to display
     */
    public void updatePlayerStats(Map<String, Integer> stats) {
        removeAllComponents();

        this.addComponent(new Label("Player Stats:"));

        stats.forEach((key, value) -> this.addComponent(new Label(key + ": " + value)));
    }
}
