package com.jr.logic;

import com.jr.TestHelper;
import com.jr.execution.HCPlayer;
import com.jr.model.IPlayOrder;
import com.jr.model.NormalPlaylist;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.FileOps;
import com.jr.util.FileOpsTest;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Galatyuk Ilya
 */
public class PlayOrderTest {
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
    public void normalOrder() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder normalOrder = new PlayOrder.Normal();
        List<Long> playHistory = new ArrayList<>();

        for (Song song : songs) {
            Song chosenSong = normalOrder.getNextSong(metal, playHistory);
            playHistory.add(chosenSong.getId());
            Assert.assertEquals(chosenSong, song);
        }
    }

    @Test
    public void shuffleTracks() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder shuffleTracks = new PlayOrder.ShuffleTracks();
        List<Long> playHistory = new ArrayList<>();
        List<Long> songIds = songs.stream().map(Song::getId).collect(Collectors.toList());

        for (int i = 0; i < songs.size(); i++) {
            Song nextSong = shuffleTracks.getNextSong(metal, playHistory);
            playHistory.add(nextSong.getId());
        }

        Assert.assertEquals(songIds.size(), playHistory.size());
        for (Long expectedId : songIds) {
            Assert.assertTrue("Expected song id " + expectedId + " not encountered in played songs", playHistory.contains(expectedId));
        }
    }

    @Test
    public void repeatTrack() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder repeatTrack = new PlayOrder.RepeatTrack();
        List<Long> playHistory = new ArrayList<>();

        Song chosenSong = repeatTrack.getNextSong(metal, playHistory);
        Assert.assertEquals(songs.get(0), chosenSong);

        playHistory.add(songs.get(2).getId());
        chosenSong = repeatTrack.getNextSong(metal, playHistory);
        Assert.assertEquals(songs.get(2), chosenSong);
    }

    @Test
    public void random_allSongsListenedTo() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder randomOrder = new PlayOrder.Random();
        List<Long> playHistory_allSongs = songs.stream().map(Song::getId).collect(Collectors.toList());

        long idExpected = playHistory_allSongs.get(0);
        HCPlayer.setMinSongsWithoutRepeat(songs.size());

        for (int i = 0; i < 5; i++) {
            Song song = randomOrder.getNextSong(metal, playHistory_allSongs);
            Assert.assertEquals(idExpected, song.getId());
        }
    }

    @Test
    public void random_songFrequency() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder randomOrder = new PlayOrder.Random();

        List<Long> playHistory = new ArrayList<>();
        playHistory.add(songs.get(0).getId());
        playHistory.add(songs.get(1).getId());
        playHistory.add(songs.get(2).getId());
        int songsQuantityToChooseFrom = songs.size() - 3;

        int totalRolls = 10000;
        int chance = totalRolls / songsQuantityToChooseFrom;

        Map<Song, Integer> expectedSongPlays = new HashMap<>();
        for (Song song : songs)
            if (!playHistory.contains(song.getId()))
                expectedSongPlays.put(song, chance);

        checkIfRandomSongsCorrespondToChances(randomOrder, metal, playHistory, totalRolls, expectedSongPlays);
    }

    private void checkIfRandomSongsCorrespondToChances(
            IPlayOrder playOrder, Playlist playlist, List<Long> playHistory, int totalRolls, Map<Song, Integer> expectedSongPlays) {

        Map<Song, Integer> timesSongsPlayed = new HashMap<>();
        for (Map.Entry<Song, Integer> chancesEntry : expectedSongPlays.entrySet())
            timesSongsPlayed.put(chancesEntry.getKey(), 0);

        for (int i = 0; i < totalRolls; i++) {
            Song song = playOrder.getNextSong(playlist, playHistory);

            Assert.assertTrue(timesSongsPlayed.containsKey(song));
            int timesAlreadyPlayed = timesSongsPlayed.get(song);
            timesSongsPlayed.put(song, timesAlreadyPlayed + 1);
        }

        for (Map.Entry<Song, Integer> timesPlayed : timesSongsPlayed.entrySet()) {
            int actualPlays = timesPlayed.getValue();
            int expectedPlays = expectedSongPlays.get(timesPlayed.getKey());
            double delta = 0.05 * expectedPlays + (totalRolls * 0.01);

            Assert.assertEquals("Song: " + timesPlayed.getKey() + ", diff " + delta, expectedPlays, actualPlays, delta);
        }
    }

    @Test
    public void weightedRandom_allSongsListenedTo() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder weightedRandomOrder = new PlayOrder.WeightedRandom();
        List<Long> playHistory = songs.stream().map(Song::getId).collect(Collectors.toList());

        HCPlayer.setMinSongsWithoutRepeat(songs.size());

        for (int i = 0; i < 10; i++) {
            int indexExpected = playHistory.size() - songs.size();
            long idExpected = playHistory.get(indexExpected);

            Song song = weightedRandomOrder.getNextSong(metal, playHistory);
            Assert.assertEquals(idExpected, song.getId());
            playHistory.add(song.getId());

        }
    }

    @Test
    public void weightedRandom_songFrequency() {
        NormalPlaylist metal = TestHelper.metalNormalPlaylist;
        List<Song> songs = metal.getSongs();
        IPlayOrder weightedRandomOrder = new PlayOrder.WeightedRandom();

        List<Long> playHistory = new ArrayList<>();
        playHistory.add(songs.get(0).getId());
        playHistory.add(songs.get(1).getId());
        playHistory.add(songs.get(2).getId());

        Map<Song, Integer> expectedSongPlays = new HashMap<>();
        Map<Song, Integer> weightMap = FlavorLogic.getWeightMap(songs, metal.getFlavor());
        int totalRolls = 0;
        for (int i = 3; i < songs.size(); i++) {
            Song iSong = songs.get(i);
            int expectedPlays = weightMap.get(iSong) / 1000;
            expectedSongPlays.put(iSong, expectedPlays);
            totalRolls += expectedPlays;
        }

        checkIfRandomSongsCorrespondToChances(weightedRandomOrder, metal, playHistory, totalRolls, expectedSongPlays);
    }
}
