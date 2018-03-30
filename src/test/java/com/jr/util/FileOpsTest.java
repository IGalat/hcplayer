package com.jr.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FileOpsTest {
    public static final String TEST_FOLDER = "src/test/resources/user/";
    private static final List<Map<String, String>> expectedGet = new ArrayList<>();

    @BeforeClass
    public static void init() {
        Map<String, String> line = new HashMap<>();
        line.put("id", "234326743");
        line.put("type", "song");
        line.put("metal", "5");
        line.put("classic", "0");
        expectedGet.add(line);

        line = new HashMap<>();
        line.put("id", "645");
        line.put("type", "playlist");
        line.put("metal", "MoreOrEqual,3");
        line.put("classic", "LessOrEqual,6");
        expectedGet.add(line);

        line = new HashMap<>();
        line.put("lala", "fafa");
        expectedGet.add(line);
    }

    @Test
    public void getNextId() {
        long initId = FileOps.getNextId();
        for (int i = 0; i < 5; i++) {
            FileOps.getNextId();
        }
        Assert.assertTrue(initId + 6 == FileOps.getNextId());
    }

    @Test
    public void put() {
        String filename = TEST_FOLDER + "putTest.txt";
        List<Map<String, String>> all = new ArrayList<>();
        Map<String, String> comment = new HashMap<>();
        Map<String, String> settings = new HashMap<>();

        comment.put("#This is teh comment", null);
        all.add(comment);

        settings.put("a", "b");
        settings.put("c", "d");
        settings.put("e", "f");
        settings.put("maxId", "123");
        all.add(settings);

        FileOps.put(filename, all, false);
    }

    @Test
    public void get() {
        String filename = TEST_FOLDER + "getTest.txt";

        List<Map<String, String>> fromFile = FileOps.getAll(filename, "playlist");

        Assert.assertTrue(mapsEqual(expectedGet.get(1), fromFile.get(0)));
    }

    @Test
    public void getAll() {
        String filename = TEST_FOLDER + "getTest.txt";

        List<Map<String, String>> fromFile = FileOps.getAll(filename);

        Assert.assertEquals(expectedGet.size(), fromFile.size());

        for (int i = 0; i < expectedGet.size(); i++) {
            Map<String, String> expectedLine = expectedGet.get(i);
            Assert.assertTrue("Fail on line " + i, mapsEqual(expectedLine, fromFile.get(i)));
        }
    }

    private boolean mapsEqual(Map<String, String> expected, Map<String, String> actual) {
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String actualValue = actual.get(entry.getKey());
            if (!entry.getValue().equals(actualValue)) return false;
        }
        return true;
    }
}
