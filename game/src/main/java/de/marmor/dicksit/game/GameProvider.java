package de.marmor.dicksit.game;

import de.marmor.dicksit.game.config.GameConfig;
import de.marmor.dicksit.game.remote.GameServer;

/**
 * Created by mbuerger on 22.03.2016.
 */
public class GameProvider {

    public static <T> Game<T> instance(GameConfig<T> gameConfig) throws GameException {
        return new GameImpl<T>(gameConfig);
    }
}
