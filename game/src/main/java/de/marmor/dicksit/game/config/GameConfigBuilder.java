package de.marmor.dicksit.game.config;

import java.util.Set;

public class GameConfigBuilder<T> {

    private final GameConfig<T> gameConfig;

    public GameConfigBuilder(Set<T> pictures) {
        gameConfig = new GameConfig<>(pictures);
    }

    public GameConfigBuilder<T> maxPlayers(int maxPlayers) {
        gameConfig.setMaxPlayers(maxPlayers);
        return this;
    }

    public GameConfigBuilder<T> endPoints(int endPoints) {
        gameConfig.setEndPoints(endPoints);
        return this;
    }

    public GameConfigBuilder<T> startCards(int startCards) {
        gameConfig.setStartCards(startCards);
        return this;
    }

    public GameConfig<T> build() {
        return gameConfig.copy();
    }
}
