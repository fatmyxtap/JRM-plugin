package com.jrmplugin.core.zip

import com.intellij.openapi.diagnostic.Logger
import com.jrmplugin.exception.UnzipArchiveTaskException
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.model.FileHeader
import java.io.File

class UnzipArchiveTask(private val destinationDirectory: String) {
    fun unzip(source: String): String {
        return try {
            val zipFile = ZipFile(source)
            zipFile.extractAll(destinationDirectory)
            destinationDirectory + File.separator + (zipFile.fileHeaders.iterator().next() as FileHeader).fileName
        } catch (ex: ZipException) {
            throw UnzipArchiveTaskException("Can't unzip file $source to destination $destinationDirectory")
        }
    }

    companion object {
        private val LOG = Logger.getInstance(UnzipArchiveTask::class.java)
    }

}