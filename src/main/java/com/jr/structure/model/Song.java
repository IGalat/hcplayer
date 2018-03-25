package com.jr.structure.model;

import lombok.Data;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 * todo (2) after (1) delete constructor, artistName, trackTitle
 */

@Data
public class Song {
    private String artistName;
    private String trackTitle;
    private String name;
    private Map<Crit, Integer> crits;

    public Song(String artistName, String trackTitle) {
        this.artistName = artistName;
        this.trackTitle = trackTitle;
    }
}
