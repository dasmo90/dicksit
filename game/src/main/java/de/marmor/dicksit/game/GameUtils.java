package de.marmor.dicksit.game;

/**
 * Created by mbuerger on 17.03.2016.
 */
public class GameUtils {

    private static int INDEX = Integer.MAX_VALUE;

    public static String generateId() {
        return Integer.toHexString(INDEX--);
    }
}
