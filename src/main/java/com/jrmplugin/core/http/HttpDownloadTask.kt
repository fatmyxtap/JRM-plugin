package com.jrmplugin.core.http

import com.intellij.openapi.diagnostic.Logger
import com.jrmplugin.exception.DownloadTaskFromServerException
import com.jrmplugin.exception.TaskByIdNotFoundException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HttpDownloadTask(private val destinationDirectory: String) {
    fun download(taskArchiveEndpoint: String, taskId: String): String {
        val fullPathToFile = destinationDirectory + File.separator + taskId + ".zip"
        val client = HttpClients.createDefault()
        val httpGet = HttpGet(taskArchiveEndpoint + taskId)
        try {
            val response = client.execute(httpGet)
            val bis = BufferedInputStream(response.entity.content)
            verifyResponseWithZipFile(taskId, bis)
            writeToDisk(fullPathToFile, bis)
            LOG.info("File successfully downloaded")
        } catch (e: IOException) {
            throw DownloadTaskFromServerException("Can't download task from server: $taskId")
        }
        return fullPathToFile
    }

    @Throws(IOException::class)
    private fun verifyResponseWithZipFile(taskId: String, bis: BufferedInputStream) {
        bis.mark(1)
        val bytesRead = bis.read(ByteArray(1))
        bis.reset()
        if (bytesRead == -1) {
            throw TaskByIdNotFoundException("Task by id " + taskId + " not found on the server. " +
                    "If you use correct task id, please, contact with admins.")
        }
    }

    @Throws(IOException::class)
    private fun writeToDisk(savedFileFullPath: String, inputStream: BufferedInputStream) {
        FileOutputStream(savedFileFullPath).use { outputStream ->
            var bytesRead: Int
            val buffer = ByteArray(BUFFER_SIZE)
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }

    companion object {
        private val LOG = Logger.getInstance(HttpDownloadTask::class.java)
        private const val BUFFER_SIZE = 4096
    }

}