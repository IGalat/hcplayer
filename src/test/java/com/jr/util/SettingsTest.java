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
        FileOps.setConfigFolder(Defaults.CONFIG_FOLDER);
    }

    @Test
    public void getNextId() {
        long initId = Settings.getNextId();
        for (int i = 0; i < 5; i++) {
            Settings.getNextId();
        }
        Assert.assertTrue(initId + 6 == Settings.getNextId());
    }

    @Test
    public void playerVolume() {
        Settings.savePlayerVolume(0.8);
        Assert.assertEquals(0.8, Settings.getPlayerVolume(), 0.0000001);

        Settings.savePlayerVolume(8);
        Assert.assertEquals(8, Settings.getPlayerVolume(), 0.0000001);
    }
}
