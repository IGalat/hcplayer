package com.jr.logic;

import com.jr.execution.HCPlayer;
import com.jr.model.IPlayOrder;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;

import java.util.Collections;
import java.util.List;
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

    private static int resultingMinSongsWoRepeat(int playlistSize) {
        int byPercentage = (int) Math.round(playlistSize * HCPlayer.getMinSongsWithoutRepeatInPlaylistPercentage());
        return Math.min(byPercentage, HCPlayer.getMinSongsWithoutRepeat());
    }


    public static class Normal implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {

            List<Song> songs = playlist.getSongs();

            Long currentSongId;
            if (playingHistory.size() > 0)
                currentSongId = playingHistory.get(playingHistory.size() - 1);
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
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            List<Song> songs = playlist.getSongs();
            if (playingHistory.size() < 2 || shuffledIds == null)
                shuffledIds = shuffleSongIds(songs);

            if (playingHistory.size() == 0)
                return SongService.getOne(shuffledIds.get(0));

            long nextSongId = getIdOfNextSong(playingHistory.get(playingHistory.size() - 1));

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
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
        }

        @Override
        public String toString() {
            return "Random";
        }
    }


    public static class WeightedRandom implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
        }

        @Override
        public String toString() {
            return "WeightedRandom";
        }
    }


    public static class RepeatTrack implements IPlayOrder {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            if (playingHistory.size() > 0)
                return SongService.getOne(playingHistory.get(playingHistory.size() - 1));
            return playlist.getSongs().get(0);
        }

        @Override
        public String toString() {
            return "RepeatTrack";
        }
    }

}
