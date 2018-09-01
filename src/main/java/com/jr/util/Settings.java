package com.jr.util;

import com.jr.execution.HCPlayer;
import com.jr.logic.FlavorLogic;
import com.jr.logic.PlayPolicy;
import com.jr.model.Flavor;
import com.jr.model.IPlayPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class Settings {
    private static final String MAX_ID_NAME = "max id";
    private static final String DEFAULT_FLAVOR_NAME = "default flavor";
    private static final String PLAYER_VOLUME_NAME = "player volume";
    private static final String PLAY_POLICY_NAME = "play policy";
    private static final String PLAYLIST_ID_NAME = "playlist id";
    private static final String MIN_SONGS_WITHOUT_REPEAT_NAME = "min songs without repeat";
    private static final String MIN_SONGS_WITHOUT_REPEAT_IN_PLAYLIST_PERCENTAGE_NAME = "min songs without repeat in playlist percentage";
    private static final String TIME_BETWEEN_SONGS_MILLISEC_NAME = "time between songs(in ms)";


    private static Map<String, String> getSettingsFromFile() {
        List<Map<String, String>> allSettingsList = FileOps.getAll(FileOps.getSettingsName());
        Map<String, String> result = new HashMap<>();

        for (Map<String, String> settingsLine : allSettingsList) {
            for (Map.Entry entry : settingsLine.entrySet()) {
                result.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return result;
    }

    private static synchronized void save(String key, String value) {
        Map<String, String> settings = getSettingsFromFile();
        settings.put(key, value);

        List<Map<String, String>> settingsListToSave = new ArrayList<>();
        for (Map.Entry entry : settings.entrySet()) {
            Map<String, String> settingsLine = new HashMap<>();
            settingsLine.put((String) entry.getKey(), (String) entry.getValue());

            settingsListToSave.add(settingsLine);
        }
        FileOps.put(FileOps.getSettingsName(), settingsListToSave, false);
    }

    private static String get(String key) {
        return getSettingsFromFile().get(key);
    }


    public static long getNextId() {
        String maxIdString = get(MAX_ID_NAME);
        Long maxId;
        if (maxIdString != null)
            maxId = Long.parseLong(maxIdString);
        else
            maxId = 1L;
        maxId++;
        save(MAX_ID_NAME, maxId.toString());
        return maxId;
    }

    public static void saveSettings() {
        savePlayerVolume(HCPlayer.getVolume());
        saveDefaultFlavor(FlavorLogic.getDefaultFlavor());
        savePlayPolicy(HCPlayer.getPlayPolicy());
        savePlaylistId(HCPlayer.getPlaylist().getId());
        saveMinSongsWithoutRepeat(HCPlayer.getMinSongsWithoutRepeat());
        saveMinSongsWithoutRepeatInPlaylistPercentage(HCPlayer.getMinSongsWithoutRepeatInPlaylistPercentage());
    }


    public static void saveDefaultFlavor(Flavor defaultFlavor) {
        save(DEFAULT_FLAVOR_NAME, defaultFlavor.toString());
    }

    public static Flavor getDefaultFlavor() {
        String defaultFlavorString = get(DEFAULT_FLAVOR_NAME);
        if (defaultFlavorString == null || defaultFlavorString.isEmpty()) return null;
        return Flavor.parse(defaultFlavorString);
    }


    public static void savePlayerVolume(double volume) {
        save(PLAYER_VOLUME_NAME, Double.toString(volume));
    }

    public static double getPlayerVolume() {
        String playerVolumeString = get(PLAYER_VOLUME_NAME);
        if (playerVolumeString == null || playerVolumeString.isEmpty()) return Defaults.PLAYER_VOLUME;
        return Double.parseDouble(playerVolumeString);
    }


    public static void savePlayPolicy(IPlayPolicy playPolicy) {
        save(PLAY_POLICY_NAME, playPolicy.toString());
    }

    public static IPlayPolicy getPlayPolicy() {
        String playPolicyString = get(PLAY_POLICY_NAME);
        if (playPolicyString == null || playPolicyString.isEmpty()) return new PlayPolicy.Normal();
        return PlayPolicy.parse(playPolicyString);
    }


    public static void savePlaylistId(long playlistId) {
        if (playlistId > 0)
            save(PLAYLIST_ID_NAME, Long.toString(playlistId));
    }

    public static Long getPlaylistId() {
        String playlistIdString = get(PLAYLIST_ID_NAME);
        if (playlistIdString == null || playlistIdString.isEmpty()) return null;
        return Long.parseLong(playlistIdString);
    }

    public static void saveMinSongsWithoutRepeat(long minSongsWithoutRepeat) {
        save(MIN_SONGS_WITHOUT_REPEAT_NAME, Long.toString(minSongsWithoutRepeat));
    }

    public static int getMinSongsWithoutRepeat() {
        String minSongsString = get(MIN_SONGS_WITHOUT_REPEAT_NAME);
        if (minSongsString == null || minSongsString.isEmpty()) return Defaults.MIN_SONGS_WITHOUT_REPEAT;
        return Integer.parseInt(minSongsString);
    }

    public static void saveMinSongsWithoutRepeatInPlaylistPercentage(double minSongs) {
        save(MIN_SONGS_WITHOUT_REPEAT_IN_PLAYLIST_PERCENTAGE_NAME, Double.toString(minSongs));
    }

    public static double getMinSongsWithoutRepeatInPlaylistPercentage() {
        String minSongsString = get(MIN_SONGS_WITHOUT_REPEAT_IN_PLAYLIST_PERCENTAGE_NAME);
        if (minSongsString == null || minSongsString.isEmpty())
            return Defaults.MIN_SONGS_WITHOUT_REPEAT_IN_PLAYLIST_PERCENTAGE;
        return Double.parseDouble(minSongsString);
    }

}
