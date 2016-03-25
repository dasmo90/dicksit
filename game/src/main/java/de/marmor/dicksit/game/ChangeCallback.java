package de.marmor.dicksit.game;

public interface ChangeCallback<T> {

    void changed(String uuid, GameState<T> gameState);
}
