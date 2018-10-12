package com.jr.service;

import com.jr.dao.MusicFolderRepositoryFile;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author Galatyuk Ilya
 */
public class MusicFolderService {
    private static final MusicFolderRepositoryFile folderRepo = new MusicFolderRepositoryFile();

    public static void add(String folder) {
        folderRepo.add(folder);
    }

    public static void remove(String folder) {
        Path folderPath = FileSystems.getDefault().getPath(folder).toAbsolutePath();
        folderRepo.getFolders().remove(folderPath);
    }

    public static void refresh() {
        folderRepo.refreshAll();
    }

}
