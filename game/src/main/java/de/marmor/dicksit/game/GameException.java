package de.marmor.dicksit.game;

public class GameException extends Exception {

    public GameException(String detailMessage) {
        super(detailMessage);
    }

    public GameException(Throwable exception) {
        super(exception);
    }
}
