package com.jrmplugin.core.http;

import com.jrmplugin.action.MainAction;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class HttpUploadTask {

    public void uploadFile(File zipToSend, String taskId) {
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", zipToSend)
                .build();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(MainAction.HOST_TASKS_URL);

        httpPost.setEntity(httpEntity);
        httpPost.addHeader("taskId", taskId);

        try {
            CloseableHttpResponse response = client.execute(httpPost);
        } catch (IOException e) {
        }
    }
}
