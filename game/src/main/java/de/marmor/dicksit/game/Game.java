package de.marmor.dicksit.game;

import de.marmor.dicksit.game.config.GameConfig;

public interface Game<T> {

    void register(ChangeCallback<T> changeCallback) throws GameException;

    void setStartPlayer(String uuid) throws GameException;

    void start() throws GameException;

    void selectTitle(String uuid, T picture, String title) throws GameException;

    void selectPicture(String uuid, T picture) throws GameException;

    void chosePicture(String uuid, T picture) throws GameException;

    void roundOkay(String uuid) throws GameException;

    enum State {
        PLAYER_SELECTING_TITLE, PLAYER_REGISTERING, PLAYER_CHOSING_PICTURE, PLAYER_SELECTING_PICTURE, ROUND_ENDED, GAME_ENDED
    }
}
