package com.jrmplugin.core;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.LocalFileSystem;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class UnzipArchiveTask {

    private static final Logger LOG = Logger.getInstance(UnzipArchiveTask.class);

    private final String destinationDirectory;

    public UnzipArchiveTask(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public boolean unzip(String source) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destinationDirectory);
            LocalFileSystem.getInstance().refreshAndFindFileByPath(destinationDirectory);
            return true;
        } catch (ZipException ex) {
            LOG.error(ex);
            return false;
        }
    }

}
