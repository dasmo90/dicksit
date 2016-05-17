package de.marmor.dicksit;

import de.marmor.dicksit.game.Game;

public class DicksitGameHolder {

    private static Game<String> instance;

    public synchronized static void setGameInstance(Game<String> game) {
        if(instance != null) {
            throw new IllegalStateException("Game instance was already set.");
        }
        instance = game;
    }

    public Game<String> getGameInstance() {
        return instance;
    }
}
