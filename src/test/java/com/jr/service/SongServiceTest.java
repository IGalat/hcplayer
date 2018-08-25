package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import javafx.util.Pair;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class SongServiceTest {

    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    @Ignore
    @Test
    public void deleteAll() {
        List<Song> songs = SongService.getAll();
        while (!songs.isEmpty()) {
            SongService.remove(songs.get(0));
        }

        //Assert.assertEquals(0, SongService.getAll().size());
    }

    @Test
    public void fillSome() {
        deleteAll();

        List<Song> expectedSongList = new ArrayList<>();
        Map<Crit, Integer> crits = new HashMap<>();

        crits.put(CritHardcode.ratingCrit, 10);
        expectedSongList.add(SongService.save(crits, "aaa"));

        expectedSongList.add(SongService.save("bbb", new Pair<>(CritHardcode.ratingCrit, 2)));

        crits = new HashMap<>();
        crits.put(CritHardcode.noveltyCrit, 33);
        expectedSongList.add(SongService.save(crits, "ccc"));

        Assert.assertEquals(expectedSongList.size(), SongService.getAll().size());

        for (Song expectedSong : expectedSongList) {
            Assert.assertEquals(expectedSong, SongService.getOne(expectedSong.getId()));
        }
    }

    @Test(expected = RuntimeException.class)
    public void outOfBoundsCrit() {
        SongService.save("Must fail to save - rating not in bounds", new Pair<>(CritHardcode.ratingCrit, 9999));
    }
}
