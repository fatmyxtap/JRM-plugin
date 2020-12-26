package com.jrmplugin.core.http;

import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.exception.DownloadTaskFromServerException;
import com.jrmplugin.exception.TaskByIdNotFoundException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HttpDownloadTask {

    private static final Logger LOG = Logger.getInstance(HttpDownloadTask.class);
    private static final int BUFFER_SIZE = 4096;

    private String destinationDirectory;

    public HttpDownloadTask(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String download(String taskArchiveEndpoint, String taskId) {
        String fullPathToFile = destinationDirectory + File.separator + taskId + ".zip";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(taskArchiveEndpoint + taskId);

        try {
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                LOG.error("Something wrong happened while downloading archive. Please try again or contact with administrator.");
                throw new DownloadTaskFromServerException("Can't download task from server: " + taskId);
            }
            BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
            verifyResponseWithZipFile(taskId, bis);
            this.writeToDisk(fullPathToFile, bis);
            LOG.info("File successfully downloaded");
        } catch (IOException e) {
            throw new DownloadTaskFromServerException("Can't download task from server: " + taskId);
        }

        return fullPathToFile;
    }

    private void verifyResponseWithZipFile(String taskId, BufferedInputStream bis) throws IOException {
        bis.mark(1);
        final int bytesRead = bis.read(new byte[1]);
        bis.reset();
        if (bytesRead == -1) {
            throw new TaskByIdNotFoundException("Task by id " + taskId + " not found on the server. " +
                    "If you use correct task id, please, contact with admins.");
        }
    }

    private void writeToDisk(String savedFileFullPath, BufferedInputStream inputStream) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(savedFileFullPath)) {
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

}
