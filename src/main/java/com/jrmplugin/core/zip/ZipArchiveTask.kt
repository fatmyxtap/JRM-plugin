package com.jrmplugin.core.zip

import com.intellij.openapi.diagnostic.Logger
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ExcludeFileHandler
import net.lingala.zip4j.model.ZipParameters
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*

class ZipArchiveTask {
    fun zip(path: String): File? {
        return try {
            val zipFile = ZipFile(createTemporaryZipFile())
            zipFile.addFolder(File(path), prepareZipParameters())
            zipFile.file
        } catch (ex: IOException) {
            LOG.error("Can't zip file: $path")
            null
        }
    }

    private fun prepareZipParameters(): ZipParameters {
        val zipParameters = ZipParameters()
        zipParameters.excludeFileHandler = ExcludeFileHandler { file: File -> file.name.contains("target") }
        return zipParameters
    }

    private fun createTemporaryZipFile(): String {
        return try {
            (Files.createTempDirectory("tmp")
                    .toAbsolutePath().toString()
                    + File.separator + UUID.randomUUID().toString() + ".zip")
        } catch (e: IOException) {
            throw IllegalStateException()
        }
    }

    companion object {
        private val LOG = Logger.getInstance(ZipArchiveTask::class.java)
    }
}