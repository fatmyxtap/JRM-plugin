package com.jrmplugin.core.zip;

import com.intellij.openapi.diagnostic.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.UUID;

public class ZipArchiveTask {

    private static final Logger LOG = Logger.getInstance(ZipArchiveTask.class);

    public File zip(String path) {
        try {
            String targetDirectory = org.assertj.core.util.Files.newTemporaryFolder().getAbsolutePath() + File.separator + UUID.randomUUID().toString();
            String fileLocation = org.assertj.core.util.Files.newTemporaryFolder() + File.separator + UUID.randomUUID().toString() + ".zip";
            ZipFile zipFile = new ZipFile(fileLocation);

            Files.walkFileTree(Paths.get(path), new HashSet<>(), Integer.MAX_VALUE, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.getFileName().toString().contains("target")) {
                        FileUtils.moveDirectory(dir.toFile(), new File(targetDirectory));
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            zipFile.addFolder(path, new ZipParameters());

            if (new File(targetDirectory).exists()) {
                FileUtils.moveDirectory(new File(targetDirectory), new File(path + File.separator + "target"));
            }

            return zipFile.getFile();
        } catch (IOException | ZipException ex) {
            LOG.error("Can't zip file: " + path);
            return null;
        }
    }

}
