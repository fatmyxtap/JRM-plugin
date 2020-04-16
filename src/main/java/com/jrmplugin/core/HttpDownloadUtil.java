package com.jrmplugin.core;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class HttpDownloadUtil {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     *
     * @param taskArchiveLink HTTP URL of the file to be downloaded
     */
    public static String downloadFile(AnActionEvent e, String taskArchiveLink) {
        String saveFilePath = null;

        try {
            VirtualFile root = ModuleRootManager.getInstance(ModuleManager.getInstance(
                    Objects.requireNonNull(e.getProject())).getModules()[0]).getContentRoots()[0];

            URL url = new URL(taskArchiveLink);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                String fileName = taskArchiveLink.substring(taskArchiveLink.lastIndexOf("/") + 1);

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                saveFilePath = root.getPath() + File.separator + fileName + ".zip";

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return saveFilePath;
    }

}
