package com.jr.service;

import com.jr.TestHelper;
import com.jr.model.Filter;
import com.jr.model.FilteredPlaylist;
import com.jr.model.Playlist;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class FilteredPlaylistServiceTest {

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
    public void deleteAll() {
        List<FilteredPlaylist> playlists = FilteredPlaylistService.getAll();
        for (int i = playlists.size(); i > 0; i--) {
            FilteredPlaylistService.remove(playlists.get(i - 1));
        }

        Assert.assertTrue(playlists.isEmpty());
    }

    @Test
    public void fillSome() {
        deleteAll();

        Filter filterNew = new Filter("[rating == null] | [genre == null] | [mood == null] | [novelty > 800]");
        FilteredPlaylistService.save("New songs", filterNew);
        Assert.assertEquals(1, FilteredPlaylistService.getAll().size());
        Assert.assertEquals(SongService.getAll(), FilteredPlaylistService.getAll().get(0).getSongs());

        Playlist anon = FilteredPlaylistService.anonPlaylist(null);
        Playlist noFiltering = FilteredPlaylistService.save("No filtering");
        Assert.assertEquals(anon.getSongs(), noFiltering.getSongs());

        Filter filterSomewhatCalm = new Filter("[calm > 4] | [wordless != null] | [classic > 7]");
        FilteredPlaylistService.save("Somewhat calm", filterSomewhatCalm);
    }
}
