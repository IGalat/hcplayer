package com.jr.service;

import com.jr.dao.FilteredPlaylistRepository;
import com.jr.dao.FilteredPlaylistRepositoryFile;
import com.jr.model.Filter;
import com.jr.model.FilteredPlaylist;
import com.jr.model.Flavor;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.Settings;
import com.jr.util.Util;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class FilteredPlaylistService {
    private static final FilteredPlaylistRepository playlistRepo = new FilteredPlaylistRepositoryFile();

    public static List<FilteredPlaylist> getAll() {
        return playlistRepo.findAll();
    }

    public static List<FilteredPlaylist> getByIds(List<Long> ids) {
        return playlistRepo.findAll(ids);
    }

    public static FilteredPlaylist getByName(String name) {
        return playlistRepo.getByName(name);
    }

    public static FilteredPlaylist getOne(long id) {
        return playlistRepo.getOne(id);
    }

    public static void remove(FilteredPlaylist playlist) {
        playlistRepo.delete(playlist);
    }

    public static FilteredPlaylist save(String name) {
        return save(name, null);
    }

    public static FilteredPlaylist save(String name, Filter filter) {
        FilteredPlaylist existingPlaylist = getByName(name);
        if (existingPlaylist != null) {
            return existingPlaylist;
        } else
            return save(name, null, true, filter, null);
    }

    public static FilteredPlaylist save(String name, Flavor flavor, boolean isDefaultFlavorUsed, Filter filter, List<Song> blacklist) {
        FilteredPlaylist existingPlaylist = playlistRepo.getByName(name);
        Long id = existingPlaylist == null ? Settings.getNextId() : existingPlaylist.getId();

        if (Util.isNameBad(name))
            throw new InputMismatchException("Bad name for playlist : '" + name + "'. Correct pattern: " + Defaults.GOOD_NAME_PATTERN);

        if (flavor == null) {
            flavor = new Flavor();
        }

        List<Song> blacklistSongs = FXCollections.observableArrayList();
        if (blacklist != null)
            blacklistSongs = FXCollections.observableArrayList(blacklist);
        else if (existingPlaylist != null && existingPlaylist.getSongs().size() > 0)
            blacklistSongs = existingPlaylist.getSongs();

        FilteredPlaylist playlist = new FilteredPlaylist(id, name, flavor, isDefaultFlavorUsed, filter, blacklistSongs);
        return playlistRepo.save(playlist);
    }

    public static FilteredPlaylist rename(FilteredPlaylist playlist, String newName) {
        if (Util.isNameBad(newName))
            throw new InputMismatchException("Cannot rename playlist '" + playlist.getName() + "' to '" + newName +
                    "'. Correct pattern: " + Defaults.GOOD_NAME_PATTERN);

        return playlistRepo.save(new FilteredPlaylist(
                playlist.getId()
                , newName
                , playlist.getFlavor()
                , playlist.isDefaultFlavorUsed()
                , playlist.getFilter()
                , playlist.getBlacklist()));
    }

    public static FilteredPlaylist addBlacklistSongs(FilteredPlaylist filteredPlaylist, Song... songs) {
        filteredPlaylist.getBlacklist().addAll(Arrays.asList(songs));
        return playlistRepo.save(filteredPlaylist);
    }

    public static FilteredPlaylist removeBlacklistSongs(FilteredPlaylist filteredPlaylist, Song... songs) {
        for (Song song : songs) {
            filteredPlaylist.getBlacklist().remove(song);
        }
        return playlistRepo.save(filteredPlaylist);
    }

    public static FilteredPlaylist anonPlaylist(Filter filter) {
        return new FilteredPlaylist(-1, "", new Flavor(), true, filter, FXCollections.observableArrayList());
    }

}
