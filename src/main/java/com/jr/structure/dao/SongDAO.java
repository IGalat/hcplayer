package com.jr.structure.dao;

import com.jr.structure.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 * todo (1) delete after test
 */
public class SongDAO {
    private static List<Song> songs = new ArrayList<>();

    static {
        songs.add(new Song("Motorhead", "Rock'n'roll"));
        songs.add(new Song("Manowar", "Sleipnir"));
        songs.add(new Song("Lordi", "Would You Love A Monsterman"));
        songs.add(new Song("Korpiklaani", "Uni"));
        songs.add(new Song("Kiyoura Natsumi", "Tabi no Tochuu (Spice and Wolf OST)"));
        songs.add(new Song("Jeremy Soule(Star Wars KOTOR)", "The Old Republic"));
        songs.add(new Song("", "The Kraken"));
    }

    public static List<Song> getSongs() {
        return songs;
    }
}
