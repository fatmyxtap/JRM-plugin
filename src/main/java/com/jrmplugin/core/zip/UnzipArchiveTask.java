package com.jrmplugin.core.zip;

import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.exception.UnzipArchiveTaskException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

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
            return destinationDirectory + File.separator + getFileName(zipFile);
        } catch (ZipException ex) {
            throw new UnzipArchiveTaskException("Can't unzip file " + source + " to destination " + destinationDirectory);
        }
    }

    public String getFileName(ZipFile zipFile) throws ZipException {
        return zipFile.getFileHeaders()
                .stream()
                .filter(f -> !f.getFileName().contains("src"))
                .filter(f -> !f.getFileName().contains("iml"))
                .filter(f -> !f.getFileName().contains("pom.xml"))
                .findAny().get().getFileName();
    }

}
