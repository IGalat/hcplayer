package com.jr.dao;

import com.jr.service.SongService;
import com.jr.util.FileOps;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * костыльненько!
 *
 * @author Galatyuk Ilya
 */
public class MusicFolderRepositoryFile {
    private static final String FOLDER_NAME = "folder";

    private static final List<Path> folders = new ArrayList<>();

    static {
        List<Map<String, String>> allFoldersMap = FileOps.getAll(FileOps.getCritsName());
        for (Map<String, String> folderMap : allFoldersMap) {
            add(folderMap.get(FOLDER_NAME));
        }
    }

    public List<Path> getFolders() {
        return folders;
    }

    public static void add(String folderName) {
        if (folderName == null) return;
        Path folder = FileSystems.getDefault().getPath(folderName).toAbsolutePath();
        if (!folders.contains(folder))
            folders.add(folder);
        rewriteFile();

        refreshFolder(folder);
    }

    public static void refreshAll() {
        for (Path folder : folders)
            refreshFolder(folder);
    }

    public static void refreshFolder(Path folder) {
        try {
            Files.find(folder, 999, (path, basicFileAttributes) -> basicFileAttributes.isRegularFile())
                    .filter(path -> path.toString().endsWith(".mp3"))
                    .forEach(path -> SongService.save(null, path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (Path folder : folders) {
            Map<String, String> mapOfFolder = new HashMap<>();
            mapOfFolder.put(FOLDER_NAME, folder.toString());
            listToSave.add(mapOfFolder);
        }
        FileOps.put(FileOps.getMusicFoldersName(), listToSave, false);
    }

}
