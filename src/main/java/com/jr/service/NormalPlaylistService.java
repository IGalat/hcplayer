package com.jr.service;

import com.jr.dao.NormalPlaylistRepository;
import com.jr.dao.NormalPlaylistRepositoryFile;
import com.jr.model.Flavor;
import com.jr.model.NormalPlaylist;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.Settings;
import com.jr.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class NormalPlaylistService {
    private static final NormalPlaylistRepository playlistRepo = new NormalPlaylistRepositoryFile();

    public static List<NormalPlaylist> getAll() {
        return playlistRepo.findAll();
    }

    public static List<NormalPlaylist> getByIds(List<Long> ids) {
        return playlistRepo.findAll(ids);
    }

    public static NormalPlaylist getByName(String name) {
        return playlistRepo.getByName(name);
    }

    public static NormalPlaylist getOne(long id) {
        return playlistRepo.getOne(id);
    }

    public static void remove(NormalPlaylist crit) {
        playlistRepo.delete(crit);
    }

    public static NormalPlaylist save(String name) {
        NormalPlaylist existingPlaylist = getByName(name);
        if (existingPlaylist != null) {
            return save(name,
                    existingPlaylist.getFlavor(),
                    existingPlaylist.isDefaultFlavorUsed(),
                    existingPlaylist.getSongs().toArray(new Song[existingPlaylist.getSongs().size()]));
        } else
            return save(name, null, (Song[]) null);
    }

    public static NormalPlaylist save(String name, Flavor flavor, Song... songs) {
        boolean isDefaultFlavorUsed = false;
        if (flavor == null) {
            isDefaultFlavorUsed = true;
        }
        return save(name, flavor, isDefaultFlavorUsed, songs);
    }

    public static NormalPlaylist save(String name, Flavor flavor, boolean isDefaultFlavorUsed, Song... songs) {
        NormalPlaylist existingPlaylist = playlistRepo.getByName(name);
        Long id = existingPlaylist == null ? Settings.getNextId() : existingPlaylist.getId();

        if (Util.isNameBad(name))
            throw new InputMismatchException("Bad name for playlist : '" + name + "'. Correct pattern: " + Defaults.GOOD_NAME_PATTERN);

        if (flavor == null) {
            flavor = new Flavor();
        }

        List<Song> songList = new ArrayList<>();
        if (songs != null)
            songList = new ArrayList<>(Arrays.asList(songs));
        if (songs == null && existingPlaylist != null && existingPlaylist.getSongs().size() > 0)
            songList = existingPlaylist.getSongs();

        NormalPlaylist playlist = new NormalPlaylist(id, name, flavor, isDefaultFlavorUsed, songList);
        return playlistRepo.save(playlist);
    }

    public static NormalPlaylist rename(NormalPlaylist playlist, String newName) {
        if (Util.isNameBad(newName))
            throw new InputMismatchException("Cannot rename playlist '" + playlist.getName() + "' to '" + newName +
                    "'. Correct pattern: " + Defaults.GOOD_NAME_PATTERN);

        return playlistRepo.save(new NormalPlaylist(
                playlist.getId()
                , newName
                , playlist.getFlavor()
                , playlist.isDefaultFlavorUsed()
                , playlist.getSongs()));
    }

    public static NormalPlaylist addSongs(NormalPlaylist normalPlaylist, Song... songs) {
        normalPlaylist.getSongs().addAll(Arrays.asList(songs));
        return playlistRepo.save(normalPlaylist);
    }

    public static NormalPlaylist removeSongs(NormalPlaylist normalPlaylist, Song... songs) {
        for (Song song : songs) {
            normalPlaylist.getSongs().remove(song);
        }
        return playlistRepo.save(normalPlaylist);
    }
}
