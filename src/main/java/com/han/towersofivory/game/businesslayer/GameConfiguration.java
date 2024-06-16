package com.han.towersofivory.game.businesslayer;

import java.io.Serializable;

public class GameConfiguration implements Serializable {
    private final String name;
    private final Gamemode gamemode;
    private final long seed;

    public GameConfiguration(String name, Gamemode gamemode, long seed) {
        this.name = name;
        this.gamemode = gamemode;
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public long getSeed() {
        return seed;
    }
}
