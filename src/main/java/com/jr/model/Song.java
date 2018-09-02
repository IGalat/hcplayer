package com.jr.model;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

public class Song {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private Path path;

    @Getter
    @Setter
    private Map<Crit, Integer> crits;

    private transient Mp3File mp3File;

    public Song(long id, Path path, Map<Crit, Integer> crits) {
        this.id = id;
        this.path = path;
        this.crits = crits;
    }

    public boolean exists() {
        try {
            File file = path.toFile();
            return file.exists() && !file.isDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Mp3File getMp3File() {
        if (mp3File == null) {
            try {
                mp3File = new Mp3File(path.toString());
            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                e.printStackTrace();
            }
        }
        return mp3File;
    }

    public ID3v2 getTags() {
        return getMp3File().getId3v2Tag();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Song{");
        sb.append("id=").append(id);
        sb.append(", path=").append(path);
        sb.append('}');
        return sb.toString();
    }
}
