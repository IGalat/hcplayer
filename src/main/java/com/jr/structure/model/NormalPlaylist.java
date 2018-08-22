package com.jr.structure.model;

import lombok.Data;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */

@Data
public class NormalPlaylist implements Playlist {
    private long id;
    private String name;
    private Flavor flavor;
    private List<Song> songs;

    @Override
    public boolean isNormal() {
        return true;
    }
}
