package com.jr.structure.service;

import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.AfterClass;
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
public class SongServiceTest extends Assert {
    @BeforeClass
    public static void init() {
        FileOps.setConfigFolder(FileOpsTest.TEST_FOLDER);
    }

    @AfterClass
    public static void close() {
        FileOps.setConfigFolder(FileOps.DEFAULT_CONFIG_FOLDER);
    }

    @Test
    public void deleteAll() {
        List<Song> songs = SongService.getAll();
        while (!songs.isEmpty()) {
            SongService.remove(songs.get(0));
        }

        assertEquals(0, SongService.getAll().size());
    }

    @Test
    public void fillSome() {
        deleteAll();

        List<Song> expectedSongList = new ArrayList<>();
        Map<Crit, Integer> crits = new HashMap<>();

        crits.put(CritService.getAll().get(0), 15);
        expectedSongList.add(SongService.save("aaa", crits));

        crits = new HashMap<>();
        crits.put(CritService.getAll().get(0), 22);
        expectedSongList.add(SongService.save("bbb", crits));

        crits = new HashMap<>();
        crits.put(CritService.getAll().get(1), 33);
        expectedSongList.add(SongService.save("ccc", crits));

        assertEquals(expectedSongList.size(), SongService.getAll().size());

        for (Song expectedSong : expectedSongList) {
            assertEquals(expectedSong, SongService.getOne(expectedSong.getId()));
        }
    }
}
