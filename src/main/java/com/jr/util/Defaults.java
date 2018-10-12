package com.jr.util;

import java.net.URISyntaxException;

/**
 * @author Galatyuk Ilya
 */
public class Defaults {
    public static final String GOOD_NAME_PATTERN = "[a-zA-Z0-9 _-]*";
    public static String CONFIG_FOLDER;//"src/main/resources/user/";
    public static final double PLAYER_VOLUME = 0.2;
    public static final int MIN_SONGS_WITHOUT_REPEAT = 5;
    public static final double MIN_SONGS_WITHOUT_REPEAT_IN_PLAYLIST_PERCENTAGE = 0.2;
    public static final int TIME_BETWEEN_SONGS_MILLIS = 250;

    static {
        String configFolder = null;
        try {
            configFolder = Defaults.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (configFolder.endsWith(".jar"))
                configFolder = configFolder.substring(0, configFolder.lastIndexOf("/") + 1);
            else {
                configFolder = configFolder.substring(0, configFolder.length() - 15);
                configFolder += "src/main/resources/";
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        CONFIG_FOLDER = configFolder + "user/";
    }
}
