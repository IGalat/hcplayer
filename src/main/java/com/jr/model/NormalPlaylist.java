package com.jr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class NormalPlaylist implements Playlist {
    private long id;
    private String name;
    private Flavor flavor;
    private boolean defaultFlavorUsed;
    private List<Song> songs;

    @Override
    public boolean isNormal() {
        return true;
    }
}
