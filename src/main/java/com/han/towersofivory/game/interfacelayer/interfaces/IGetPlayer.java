package com.han.towersofivory.game.interfacelayer.interfaces;

import com.han.towersofivory.game.businesslayer.entities.Player;

import java.util.UUID;

public interface IGetPlayer {
    public Player getplayer(UUID uuid);
}
