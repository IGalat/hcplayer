package com.jr.util;

import com.jr.logic.CritHardcode;

import java.util.regex.Pattern;

/**
 * @author Galatyuk Ilya
 */
public class Util {
    public static final String GOOD_NAME_PATTERN = "[a-zA-Z0-9 _]*";
    private static boolean isInitialized = false;

    public static int roll(int max) {
        return (int) (Math.random() * max + 1);
    }

    public static boolean isNameBad(String name) {
        return !Pattern.matches(GOOD_NAME_PATTERN, name);
    }

    public static void init() {
        if (isInitialized) return;

        CritHardcode.saveStandardCrits();

        isInitialized = true;
    }
}
