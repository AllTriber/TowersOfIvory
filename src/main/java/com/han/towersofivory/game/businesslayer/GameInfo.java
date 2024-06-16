package com.han.towersofivory.game.businesslayer;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final GameConfiguration gameConfiguration;
    private int numberOfPlayers;

    public GameInfo(GameConfiguration gameConfiguration, int numberOfPlayers) {
        this.gameConfiguration = gameConfiguration;
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    public String getGameName() {
       return gameConfiguration.getName();
    }
}
