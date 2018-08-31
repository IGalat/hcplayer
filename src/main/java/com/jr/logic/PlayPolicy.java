package com.jr.logic;

import com.jr.model.IPlayPolicy;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class PlayPolicy {

    public static IPlayPolicy parse(String policyName) {
        switch (policyName) {
            case "Normal":
                return new Normal();
            case "ShuffleSongs":
                return new ShuffleSongs();
            case "Random":
                return new Random();
            case "WeightedRandom":
                return new WeightedRandom();
            default:
                return new Normal();
        }
    }


    public static class Normal implements IPlayPolicy {

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


    public static class ShuffleSongs implements IPlayPolicy {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
        }

        @Override
        public String toString() {
            return "ShuffleSongs";
        }
    }


    public static class Random implements IPlayPolicy {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
        }

        @Override
        public String toString() {
            return "Random";
        }
    }


    public static class WeightedRandom implements IPlayPolicy {

        @Override
        public Song getNextSong(Playlist playlist, List<Long> playingHistory) {
            return null; //todo
        }

        @Override
        public String toString() {
            return "WeightedRandom";
        }
    }

}
