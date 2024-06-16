package com.han.towersofivory.game.businesslayer;

import java.io.Serializable;

public enum Gamemode implements Serializable {
    LMS("Last Man Standing"),
    CTF("Capture the Flag");

    private final String fullName;

    Gamemode(String fullName) {
        this.fullName = fullName;
    }

    public static Gamemode fromFullName(String fullName) {
        for (Gamemode gamemode : Gamemode.values()) {
            if (gamemode.getFullName().equals(fullName)) {
                return gamemode;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant " + Gamemode.class.getCanonicalName() + " with full name " + fullName
        );
    }

    public String getFullName() {
        return fullName;
    }
}
