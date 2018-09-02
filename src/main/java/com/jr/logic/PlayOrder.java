package com.jr.logic;

import com.jr.execution.HCPlayer;
import com.jr.model.IPlayOrder;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;

import java.util.List;

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

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
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
