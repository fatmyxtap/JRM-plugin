package com.jrmplugin.core.zip;

import com.intellij.openapi.diagnostic.Logger;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.util.Files.newTemporaryFolder;

public class ZipArchiveTask {

    private static final Logger LOG = Logger.getInstance(ZipArchiveTask.class);

    public File zip(String path) {
        try {
            ZipFile zipFile = new ZipFile(createTemporaryZipFile());
            zipFile.addFolder(new File(path), prepareZipParameters());
            return zipFile.getFile();
        } catch (IOException ex) {
            LOG.error("Can't zip file: " + path);
            return null;
        }
    }

    @NotNull
    private ZipParameters prepareZipParameters() {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setExcludeFileHandler(
                file -> file.getName().contains("target")
        );
        return zipParameters;
    }

    @NotNull
    private String createTemporaryZipFile() {
        return newTemporaryFolder() + File.separator + UUID.randomUUID().toString() + ".zip";
    }

}
