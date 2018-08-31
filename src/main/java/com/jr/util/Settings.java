package com.jr.util;

import com.jr.execution.HCPlayer;
import com.jr.logic.FlavorLogic;
import com.jr.model.Flavor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class Settings {
    private static final String MAX_ID_NAME = "maxId";
    private static final String DEFAULT_FLAVOR_NAME = "default flavor";
    private static final String PLAYER_VOLUME_NAME = "player volume";

    private static synchronized Map<String, String> getSettingsFromFile() {
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

    private static synchronized String get(String key) {
        return getSettingsFromFile().get(key);
    }


    public static synchronized long getNextId() {
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

    public static synchronized void saveSettings() {
        savePlayerVolume(HCPlayer.getVolume());
        saveDefaultFlavor(FlavorLogic.getDefaultFlavor());
    }

    public static synchronized void saveDefaultFlavor(Flavor defaultFlavor) {
        save(DEFAULT_FLAVOR_NAME, defaultFlavor.toString());
    }

    public static synchronized Flavor getDefaultFlavor() {
        String defaultFlavorString = get(DEFAULT_FLAVOR_NAME);
        if (defaultFlavorString == null || defaultFlavorString.isEmpty()) return null;
        return Flavor.parse(defaultFlavorString);
    }

    public static synchronized void savePlayerVolume(double volume) {
        save(PLAYER_VOLUME_NAME, Double.toString(volume));
    }

    public static synchronized double getPlayerVolume() {
        String playerVolumeString = get(PLAYER_VOLUME_NAME);
        if (playerVolumeString == null || playerVolumeString.isEmpty()) return Defaults.PLAYER_VOLUME;
        return Double.parseDouble(playerVolumeString);
    }

}
