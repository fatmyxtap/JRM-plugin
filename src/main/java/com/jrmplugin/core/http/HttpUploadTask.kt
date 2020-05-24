package com.jrmplugin.core.http

import com.intellij.openapi.diagnostic.Logger
import com.jrmplugin.action.MainAction
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class HttpUploadTask {
    fun uploadFile(zipToSend: File?, taskId: String?): String? {
        val httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", zipToSend)
                .build()
        val client = HttpClients.createDefault()
        val httpPost = HttpPost(MainAction.HOST_TASKS_URL)
        httpPost.entity = httpEntity
        httpPost.addHeader("taskId", taskId)
        return try {
            val response = client.execute(httpPost)
            val responseEntity = response.entity
            val entity = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8)
            LOG.info(entity)
            entity
        } catch (e: IOException) {
            LOG.error(e)
            null
        }
    }

    companion object {
        private val LOG = Logger.getInstance(HttpUploadTask::class.java)
    }
}