package com.jr.model;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface IPlayOrder {

    Song getNextSong(Playlist playlist, List<Long> playingHistory);

    @Override
    String toString();

}
