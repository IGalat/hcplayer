package com.jr.logic;

import com.jr.execution.HCPlayer;
import com.jr.model.Flavor;
import com.jr.model.IPlayOrder;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;
import com.jr.util.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Galatyuk Ilya
 */
public class PlayOrder {

    public static IPlayOrder parse(String playOrderName) {
        switch (playOrderName) {
            case "Normal":
                return new Normal();
            case "ShuffleTracks":
                return new ShuffleTracks();
            case "Random":
                return new Random();
            case "WeightedRandom":
                return new WeightedRandom();
            case "RepeatTrack":
                return new RepeatTrack();
            default:
                return new Normal();
        }
    }

    public static final List<String> playOrdersArray = Arrays.asList(
            "Normal", "ShuffleTracks", "Random", "WeightedRandom", "RepeatTrack"
    );

    private static List<Song> getPlayableSongs(List<Song> songs, List<Long> playHistory) {
        List<Song> playableSongs = new ArrayList<>();
        List<Long> nonPlayableIds = new ArrayList<>();
        int playHistorySize = playHistory.size();

        int byPercentage = (int) Math.round(songs.size() * HCPlayer.getMinSongsWithoutRepeatInPlaylistPercentage());
        int quantityOfNonPlayable = Math.max(byPercentage, HCPlayer.getMinSongsWithoutRepeat());
        if (quantityOfNonPlayable >= songs.size()) quantityOfNonPlayable = songs.size() - 1;

        for (int i = 1; i <= quantityOfNonPlayable; i++) {
            int index = playHistorySize - i;
            if (index < 0) break;
            nonPlayableIds.add(playHistory.get(index));
        }

        for (Song song : songs)
            if (song != null && !nonPlayableIds.contains(song.getId())) {
                playableSongs.add(song);
            }

        return playableSongs;
    }

    private static int resultingMinSongsWoRepeat(int playlistSize) {
        int byPercentage = (int) Math.round(playlistSize * HCPlayer.getMinSongsWithoutRepeatInPlaylistPercentage());
        return Math.min(byPercentage, HCPlayer.getMinSongsWithoutRepeat());
    }


    public static class Normal implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playHistory) {

            List<Song> songs = playlist.getSongs();

            Long currentSongId;
            if (playHistory.size() > 0)
                currentSongId = playHistory.get(playHistory.size() - 1);
            else
                return songs.get(0);

            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getId() == currentSongId) {
                    if (i == songs.size() - 1)
                        return songs.get(0);
                    else
                        return songs.get(i + 1);
                }
            }

            return SongService.getOne(currentSongId); //if this playlist doesn't contain this song, it repeats
        }

        @Override
        public String toString() {
            return "Normal";
        }
    }


    public static class ShuffleTracks implements IPlayOrder {
        private List<Long> shuffledIds;

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playHistory) {
            List<Song> songs = playlist.getSongs();
            if (playHistory.size() < 2 || shuffledIds == null)
                shuffledIds = shuffleSongIds(songs);

            if (playHistory.size() == 0)
                return SongService.getOne(shuffledIds.get(0));

            long nextSongId = getIdOfNextSong(playHistory.get(playHistory.size() - 1));

            return SongService.getOne(nextSongId);
        }

        private long getIdOfNextSong(long idOfPlayingSong) {
            int indexOfPlaying = 0;
            for (int i = 0; i < shuffledIds.size(); i++) {
                if (idOfPlayingSong == shuffledIds.get(i)) {
                    indexOfPlaying = i;
                    break;
                }
            }
            int indexNext;
            if (indexOfPlaying == shuffledIds.size() - 1)
                indexNext = 0;
            else indexNext = indexOfPlaying + 1;

            return shuffledIds.get(indexNext);
        }

        private List<Long> shuffleSongIds(List<Song> songs) {
            List<Long> ids = songs.stream().map(Song::getId).collect(Collectors.toList());
            Collections.shuffle(ids);
            return ids;
        }

        @Override
        public String toString() {
            return "ShuffleTracks";
        }
    }


    public static class Random implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playHistory) {
            List<Song> playableSongs = getPlayableSongs(playlist.getSongs(), playHistory);
            int index = (int) Util.roll(playableSongs.size()) - 1;
            return playableSongs.get(index);
        }

        @Override
        public String toString() {
            return "Random";
        }
    }


    public static class WeightedRandom implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playHistory) {
            Flavor flavor = playlist.isDefaultFlavorUsed() ? FlavorLogic.getDefaultFlavor() : playlist.getFlavor();
            List<Song> playableSongs = getPlayableSongs(playlist.getSongs(), playHistory);
            Map<Song, Integer> weightMap = FlavorLogic.getWeightMap(playableSongs, flavor);

            long maxRoll = 0;
            for (Map.Entry<Song, Integer> weight : weightMap.entrySet())
                maxRoll += weight.getValue();
            long roll = Util.roll(maxRoll);

            for (Map.Entry<Song, Integer> weight : weightMap.entrySet()) {
                roll -= weight.getValue();
                if (roll <= 0) return weight.getKey();
            }

            return null; //shouldn't go here at all: logic error if did
        }

        @Override
        public String toString() {
            return "WeightedRandom";
        }
    }


    public static class RepeatTrack implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playHistory) {
            if (playHistory.size() > 0)
                return SongService.getOne(playHistory.get(playHistory.size() - 1));
            return playlist.getSongs().get(0);
        }

        @Override
        public String toString() {
            return "RepeatTrack";
        }
    }

}
