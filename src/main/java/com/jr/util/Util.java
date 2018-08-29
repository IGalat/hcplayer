package com.jr.util;

import com.jr.dao.CritRepositoryFile;
import com.jr.dao.NormalPlaylistRepositoryFile;
import com.jr.dao.SongRepositoryFile;
import com.jr.logic.CritHardcode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * @author Galatyuk Ilya
 */
public class Util {
    public static final String GOOD_NAME_PATTERN = "[a-zA-Z0-9 _-]*";
    private static boolean isInitialized = false;
    private static final Logger log = LogManager.getLogger(Util.class);

    public static long roll(long max) {
        return (long) (Math.random() * max + 1);
    }

    public static boolean isNameBad(String name) {
        return !Pattern.matches(GOOD_NAME_PATTERN, name);
    }

    public static void init() {
        if (isInitialized) return;

        log.info("init() called");
        CritHardcode.saveStandardCrits();

        isInitialized = true;
    }

    public static void shutdown() {
        saveData();
    }

    public static void saveData() {
        CritRepositoryFile.rewriteFile();
        SongRepositoryFile.rewriteFile();
        NormalPlaylistRepositoryFile.rewriteFile();
    }
    
}
