package com.jr.util;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Galatyuk Ilya
 */
public class SettingsTest {

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    @Test
    public void getNextId() {
        long initId = Settings.getNextId();
        for (int i = 0; i < 5; i++) {
            Settings.getNextId();
        }
        Assert.assertTrue(initId + 6 == Settings.getNextId());
    }
}
