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
    private static volatile long maxId;
    public static final String CONFIG_FOLDER = "src/main/resources/user/";
    public static final String SETTINGS = CONFIG_FOLDER + "settings.txt";
    public static final String CRITS = CONFIG_FOLDER + "crits.txt";
    public static final String SONGS = CONFIG_FOLDER + "songs.txt";
    public static final String PLAYLISTS = CONFIG_FOLDER + "playlists.txt";

    static {
        List<Map<String, String>> settings = getAll(SETTINGS);
        for (Map<String, String> settingsLine : settings)
            for (Map.Entry<String, String> setting : settingsLine.entrySet()) {

                if (setting.getKey().equals("maxId"))
                    maxId = Long.parseLong(setting.getValue());
                //other values here
            }
        try {
            new File("sample2").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized long getNextId() {
        return ++maxId;
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
