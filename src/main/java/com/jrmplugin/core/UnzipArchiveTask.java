package com.jrmplugin.core;

import com.intellij.openapi.diagnostic.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;

public class UnzipArchiveTask {

    private static final Logger LOG = Logger.getInstance(UnzipArchiveTask.class);

    private final String destinationDirectory;

    public UnzipArchiveTask(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String unzip(String source) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destinationDirectory);
            return destinationDirectory + File.separator + ((FileHeader) zipFile.getFileHeaders().iterator().next()).getFileName();
        } catch (ZipException ex) {
            LOG.error("Can't unzip file: " + source);
            return null;
        }
    }

}
