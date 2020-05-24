package com.jrmplugin.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jrmplugin.core.ProcessableMouseAdapter
import com.jrmplugin.core.ProjectStoreComponent
import com.jrmplugin.core.http.HttpUploadTask
import com.jrmplugin.core.zip.ZipArchiveTask
import com.jrmplugin.dto.ServerTaskResult
import com.jrmplugin.dto.ServerTaskResult.Status.*
import com.jrmplugin.ui.PluginMainPopupWindow
import java.io.IOException

class CompleteTaskButtonListener(private val pluginMainPopupWindow: PluginMainPopupWindow)
    : ProcessableMouseAdapter(PluginMainPopupWindow.completeTaskButton) {
    private val projectStoreComponent: ProjectStoreComponent = ProjectStoreComponent()
    private val zipArchiveTask: ZipArchiveTask = ZipArchiveTask()
    private val httpUploadTask: HttpUploadTask = HttpUploadTask()

    override fun actionPerformed() {
        val taskSourcesLocation = projectStoreComponent[pluginMainPopupWindow.taskId]
        val zipToSend = zipArchiveTask.zip(taskSourcesLocation!!)
        val result = parseResult(
                httpUploadTask.uploadFile(zipToSend, pluginMainPopupWindow.taskId)
        )
        PluginMainPopupWindow.resultTextField.text = result
    }

    private fun parseResult(result: String?): String {
        return if (result == null) {
            "Server didn't answer on our request! Please, contact with admins."
        } else {
            val objectMapper = ObjectMapper()
            try {
                val serverTaskResult = objectMapper.readValue(result, ServerTaskResult::class.java)
                when (serverTaskResult.status) {
                    COMPLETED -> "Completed! Go to next task!"
                    UNCOMPLETED -> "Some errors in your task, check please: " + serverTaskResult.message
                    IN_PROGRESS -> "Task in progress, please wait!"
                    FAILURE -> "Compilation error, check please error message: " + serverTaskResult.message
                    else -> "Incorrect answer from server on our request! Please, contact with admins."
                }
            } catch (e: IOException) {
                "Incorrect answer from server on our request! Please, contact with admins."
            }
        }
    }

}