package com.jrmplugin.core;

import com.intellij.openapi.diagnostic.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloadTask {

    private static final Logger LOG = Logger.getInstance(HttpDownloadTask.class);
    private static final int BUFFER_SIZE = 4096;

    private String destinationDirectory;

    public HttpDownloadTask(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String download(String taskArchiveLink) {
        String fullPathToFile = null;

        try {
            URL url = new URL(taskArchiveLink);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            // always check HTTP response code first
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String taskId = taskArchiveLink.substring(taskArchiveLink.lastIndexOf("/") + 1);
                fullPathToFile = destinationDirectory + File.separator + taskId + ".zip";

                this.writeToDisk(fullPathToFile, httpConn);

                LOG.info("File successfully downloaded");
            } else {
                LOG.error("No file to download. Server replied HTTP code: " + httpConn.getResponseCode());
            }
            httpConn.disconnect();
        } catch (Exception ex) {
            LOG.error(ex);
        }
        return fullPathToFile;
    }

    private void writeToDisk(String savedFileFullPath, HttpURLConnection httpConn) throws IOException {
        // opens input stream from the HTTP connection
        try (InputStream inputStream = httpConn.getInputStream()) {
            // opens an output stream to save into file
            try (FileOutputStream outputStream = new FileOutputStream(savedFileFullPath)) {
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }

}
