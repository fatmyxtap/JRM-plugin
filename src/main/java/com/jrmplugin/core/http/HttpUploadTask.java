package com.jrmplugin.core.http;

import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.action.MainAction;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpUploadTask {

    private static final Logger LOG = Logger.getInstance(HttpUploadTask.class);

    public String uploadFile(File zipToSend, String taskId) {
        var httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", zipToSend)
                .build();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(MainAction.CORE_PROPERTIES.getProperty("api.processingUrl") + taskId);
        httpPost.setEntity(httpEntity);

        try {
            CloseableHttpResponse response = client.execute(httpPost);
            var responseEntity = response.getEntity();
            String entity = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            LOG.info(entity);
            return entity;
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }
}
