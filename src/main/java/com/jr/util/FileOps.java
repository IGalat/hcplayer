package com.jr.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class FileOps {
    public static final String DEFAULT_CONFIG_FOLDER = "src/main/resources/user/";
    private static String configFolder = DEFAULT_CONFIG_FOLDER;
    private static String critsName = "crits.txt";
    private static String songsName = "songs.txt";
    private static String playlistsName = "playlists.txt";
    private static String settingsName = "settings.txt";


    public static synchronized String getConfigFolder() {
        return configFolder;
    }

    public static synchronized void setConfigFolder(String configFolder) {
        FileOps.configFolder = configFolder;
    }

    public static String getCritsName() {
        return configFolder + critsName;
    }

    public static String getSongsName() {
        return configFolder + songsName;
    }

    public static String getPlaylistsName() {
        return configFolder + playlistsName;
    }

    public static String getSettingsName() {
        return configFolder + settingsName;
    }

    public static synchronized void put(String filename, Map<String, String> content, boolean append) {
        List<Map<String, String>> contents = new ArrayList<>();
        contents.add(content);
        put(filename, contents, append);
    }

    public static synchronized void put(String filename, List<Map<String, String>> contents, boolean append) {
        StringBuffer result = new StringBuffer();
        for (Map<String, String> contentLine : contents) {
            for (Map.Entry<String, String> entry : contentLine.entrySet()) {
                result.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append(";");
            }

            result.append("\n");
        }
        writeToFile(filename, result.toString(), append);
    }

    private static synchronized void writeToFile(String filename, String content, boolean append) {
        mkFile(filename);
        try (FileWriter fileWriter = new FileWriter(filename, append)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File mkFile(String filename) {
        int slashIndex = Math.max(filename.lastIndexOf('\\'), filename.lastIndexOf('/'));
        File dir = null;
        if (slashIndex > -1)
            dir = new File(filename.substring(0, slashIndex));
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            return file;
        }

        try {
            if (slashIndex > -1)
                dir.mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static synchronized List<Map<String, String>> getAll(String filename) {
        return getAll(filename, "");
    }

    public static synchronized List<Map<String, String>> getAll(String filename, String search) {
        List<String> content = new ArrayList<>();
        List<Map<String, String>> filtered = new ArrayList<>();

        mkFile(filename);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null)
                content.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : content) {
            line = removeComments(line);
            if (line.isEmpty()) continue;
            if (line.contains(search)) {
                Map<String, String> lineMap = new HashMap<>();
                String[] entries = line.split(";");
                for (String entry : entries) {
                    String[] pair = entry.split("=");
                    lineMap.put(pair[0], pair[1]);
                }
                filtered.add(lineMap);
            }
        }

        return filtered;
    }

    private static String removeComments(String line) {
        String[] commentOpts = new String[]{"#", "//"};

        for (String comment : commentOpts)
            if (line.contains(comment))
                line = line.substring(0, line.indexOf(comment));

        return line.trim();
    }

}
