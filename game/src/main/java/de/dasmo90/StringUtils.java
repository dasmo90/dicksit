package de.dasmo90;

/**
 * Created by mbuerger on 20.03.2016.
 */
public class StringUtils {

    public static boolean isBlank(String string) {
        return (string == null || "".equals(string));
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static String set(String value, String defaultValue) {
        return isNotBlank(value) ? value : defaultValue;
    }
}
