package com.jr.structure.model;

import com.jr.structure.model.sub.Flavor;
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
    private List<Long> songIds;
    private static final String TYPE = "Normal";
}
