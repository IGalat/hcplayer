package com.jr.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FileOpsTest {
    private static final String TEST_PREFIX = "test\\";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void getNextIdTest() {
        long initId = FileOps.getNextId();
        for (int i = 0; i < 5; i++) {
            FileOps.getNextId();
        }
        Assert.assertTrue(initId + 6 == FileOps.getNextId());
    }

    @Test
    public void putTest() throws IOException {
        String filename = TEST_PREFIX + FileOps.SETTINGS_FILENAME;
        Map<String, String> settings = new HashMap<>();

        settings.put("a", "b");
        settings.put("c", "d");
        settings.put("e", "f");
        settings.put("maxId", "123");

        File dir = temporaryFolder.newFolder(TEST_PREFIX);
        dir = temporaryFolder.newFolder(TEST_PREFIX, FileOps.CONFIG_FOLDER);
        File file = temporaryFolder.newFile(filename);

        FileOps.put(file.getAbsolutePath(), settings, false);
    }
}
