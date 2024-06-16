package com.han.towersofivory.game.businesslayer.worldgeneration.world;

import com.han.towersofivory.game.businesslayer.GameInfo;
import com.han.towersofivory.game.businesslayer.entities.Player;
import com.han.towersofivory.game.businesslayer.entities.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GameState is a class to hold all de data of a game.
 * It holds the gameInfo of a game and the plauers with its positions.
 * The class can be used to get and set info about the game.
 */
public class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private GameInfo gameInfo;
    private final List<Player> players;
    private int lowestFloorLevel;
    private List<Position> pickedUpItemsPositions;

    public GameState(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.players = new ArrayList<>();
        this.pickedUpItemsPositions = new ArrayList<>();
    }

    /*
     * UC4 Deelnemen Spel
     *
     * Adds a player to the gamestate.
     *
     * @param myPlayer - The player to be added
     * @return void
     */
    public void addPlayer(Player myPlayer) {
        myPlayer.setPlayerVision(null);
        players.add(myPlayer);
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public String getGameName() {
        return gameInfo.getGameName();
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public int getLowestFloorLevel() {
        return lowestFloorLevel;
    }

    public void setLowestFloorLevel(int lowestFloorLevel) {
        this.lowestFloorLevel = lowestFloorLevel;
    }

    public void setPickedUpItemsPositions(List<Position> pickedUpItemsPositions) {
        this.pickedUpItemsPositions = pickedUpItemsPositions;
    }

    public List<Position> getPickedUpItemsPositions() {
        return pickedUpItemsPositions;
    }
}
