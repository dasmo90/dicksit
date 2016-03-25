package de.marmor.dicksit.game.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mbuerger on 20.03.2016.
 */
public class GameConfig<T> {

    private Set<T> pictures;
    private int startCards = 6;
    private int maxPlayers = 6;
    private int endPoints = 30;

    public GameConfig(Set<T> pictures) {
        this.pictures = new HashSet<>(pictures);
    }

    public Set<T> getPictures() {
        return new HashSet<>(pictures);
    }

    public int getStartCards() {
        return startCards;
    }

    protected void setStartCards(int startCards) {
        this.startCards = startCards;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    protected void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(int endPoints) {
        this.endPoints = endPoints;
    }

    protected GameConfig<T> copy() {
        GameConfig<T> gameConfigCopy = new GameConfig<>(this.getPictures());
        gameConfigCopy.setStartCards(this.getStartCards());
        gameConfigCopy.setEndPoints(this.getEndPoints());
        gameConfigCopy.setMaxPlayers(this.getMaxPlayers());
        return gameConfigCopy;
    }
}
