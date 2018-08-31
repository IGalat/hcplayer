package com.jr.execution;

import com.jr.TestHelper;
import com.jr.service.SongService;
import com.jr.util.*;
import org.junit.*;

/**
 * @author Galatyuk Ilya
 */
public class MediaPlayerAdapterTest {

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @Before
    public void initMethod() {
        TestHelper.setStandardTestData();
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(Defaults.CONFIG_FOLDER);
    }

    @Test
    public void volume() {
        MediaPlayerAdapter.setVolume(0.6);
        Assert.assertEquals(0.6, MediaPlayerAdapter.getVolume(), 0.0000001);

        MediaPlayerAdapter.setVolume(8);
        Util.saveData();
        Assert.assertEquals(1, Settings.getPlayerVolume(), 0.0000001);
    }

    @Test
    public void play() {
        MediaPlayerAdapter.setVolume(0.1);
        MediaPlayerAdapter.play(SongService.getAll().get(4).getPath());
    }
}
