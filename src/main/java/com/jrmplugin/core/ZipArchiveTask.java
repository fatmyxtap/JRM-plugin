package com.jrmplugin.core;

import com.intellij.openapi.diagnostic.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.assertj.core.util.Files;

import java.io.File;
import java.util.UUID;

public class ZipArchiveTask {

    private static final Logger LOG = Logger.getInstance(ZipArchiveTask.class);

    public File zip(String path) {
        try {
            String fileLocation = Files.newTemporaryFolder() + File.separator + UUID.randomUUID().toString() + ".zip";
            ZipFile zipFile = new ZipFile(fileLocation);
            zipFile.createZipFile(new File(path), new ZipParameters());
            return zipFile.getFile();
        } catch (ZipException ex) {
            LOG.error("Can't zip file: " + path);
            return null;
        }
    }

}
