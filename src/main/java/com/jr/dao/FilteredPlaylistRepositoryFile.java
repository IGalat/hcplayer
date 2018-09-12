package com.jr.dao;

import com.jr.model.Filter;
import com.jr.model.FilteredPlaylist;
import com.jr.model.Flavor;
import com.jr.model.Song;
import com.jr.util.FileOps;
import com.jr.util.Util;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FilteredPlaylistRepositoryFile implements FilteredPlaylistRepository {
    private static final String ID_NAME = "id";
    private static final String NAME_NAME = "name";
    private static final String FLAVOR_NAME = "flavor";
    private static final String IS_DEFAULT_FLAVOR_USED_NAME = "is default flavor";
    private static final String BLACKLIST_NAME = "blacklist";
    private static final String FILTER_NAME = "filter";
    private static final List<FilteredPlaylist> playlists;

    static {
        List<Map<String, String>> allPlaylistsMap = FileOps.getAll(FileOps.getFilteredPlaylistsName());
        playlists = FXCollections.observableArrayList();

        for (Map<String, String> playlistMap : allPlaylistsMap) {
            long id = Long.parseLong(playlistMap.get(ID_NAME));
            String name = playlistMap.get(NAME_NAME);
            String flavorMap = playlistMap.get(FLAVOR_NAME);
            boolean isDefaultFlavorUsed = Boolean.parseBoolean(playlistMap.get(IS_DEFAULT_FLAVOR_USED_NAME));
            Flavor flavor = Flavor.parse(flavorMap);
            List<Song> blacklist = Util.parseSongsFromIds(playlistMap.get(BLACKLIST_NAME));
            Filter filter = new Filter(playlistMap.get(FILTER_NAME));

            playlists.add(new FilteredPlaylist(id, name, flavor, isDefaultFlavorUsed, filter, blacklist));
        }
    }


    @Override
    public List<FilteredPlaylist> findAll() {
        return playlists;
    }

    @Override
    public List<FilteredPlaylist> save(Iterable<FilteredPlaylist> items) {
        for (FilteredPlaylist playlistToSave : items) {
            for (int i = playlists.size(); i > 0; i--) {
                FilteredPlaylist playlist = playlists.get(i - 1);
                if (playlistToSave.getId() == playlist.getId()
                        || playlistToSave.getName().equals(playlist.getName())) {
                    playlistToSave.setId(playlist.getId());
                    playlists.remove(playlist);
                }
            }
            playlists.add(playlistToSave);
        }
        rewriteFile();
        return (List<FilteredPlaylist>) items;
    }

    @Override
    public void delete(Iterable<FilteredPlaylist> items) {
        for (FilteredPlaylist playlistToRemove : items) {
            playlists.remove(playlistToRemove);
        }
        rewriteFile();
    }

    public static synchronized void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (FilteredPlaylist playlist : playlists) {
            Map<String, String> mapOfPlaylist = new HashMap<>();

            mapOfPlaylist.put(ID_NAME, Long.toString(playlist.getId()));
            mapOfPlaylist.put(NAME_NAME, playlist.getName());
            if (playlist.getFlavor().getFlavorMap() != null)
                mapOfPlaylist.put(FLAVOR_NAME, playlist.getFlavor().toString());
            mapOfPlaylist.put(IS_DEFAULT_FLAVOR_USED_NAME, Boolean.toString(playlist.isDefaultFlavorUsed()));

            if (playlist.getBlacklist().size() > 0) {
                StringBuilder songIds = new StringBuilder();
                for (Song song : playlist.getSongs())
                    if (song != null)
                        songIds.append(song.getId()).append(",");
                songIds.deleteCharAt(songIds.length() - 1);
                mapOfPlaylist.put(BLACKLIST_NAME, songIds.toString());
            }

            if (playlist.getFilter() != null && !playlist.getFilter().getLogicExpression().isEmpty())
                mapOfPlaylist.put(FILTER_NAME, playlist.getFilter().toString());

            listToSave.add(mapOfPlaylist);
        }

        FileOps.put(FileOps.getFilteredPlaylistsName(), listToSave, false);
    }

}
