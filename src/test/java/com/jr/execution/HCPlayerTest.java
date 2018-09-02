package com.jr.execution;

import com.jr.TestHelper;
import com.jr.logic.PlayOrder;
import com.jr.model.NormalPlaylist;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

/**
 * @author Galatyuk Ilya
 */
public class HCPlayerTest {

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
    public void playingNormal() {
        MediaPlayerAdapter.setVolume(0);
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        HCPlayer.setPlayOrder(new PlayOrder.Normal());
        HCPlayer.playPlaylist(metal, metal.getSongs().get(3));

        for (int i = 3; i < 6; i++) {
            Assert.assertEquals(metal.getSongs().get(i), HCPlayer.getCurrentSong());
            HCPlayer.playNextSong();
        }
    }
}
