package com.jrmplugin.listener

import com.intellij.openapi.actionSystem.AnActionEvent
import com.jrmplugin.action.MainAction
import com.jrmplugin.core.ProcessableMouseAdapter
import com.jrmplugin.core.ProjectStoreComponent
import com.jrmplugin.core.VirtualTreeRefreshTask
import com.jrmplugin.core.http.HttpDownloadTask
import com.jrmplugin.core.zip.UnzipArchiveTask
import com.jrmplugin.ui.PluginMainPopupWindow
import com.jrmplugin.util.CoreUtil
import java.io.File

class FetchTaskButtonListener(event: AnActionEvent, private val pluginMainPopupWindow: PluginMainPopupWindow)
    : ProcessableMouseAdapter(PluginMainPopupWindow.fetchTaskButton) {
    private val httpDownloadTask: HttpDownloadTask = HttpDownloadTask(CoreUtil.getProjectFolder(event))
    private val unzipArchiveTask: UnzipArchiveTask = UnzipArchiveTask(CoreUtil.getProjectFolder(event))
    private val virtualTreeRefreshTask: VirtualTreeRefreshTask = VirtualTreeRefreshTask()
    private val projectStoreComponent: ProjectStoreComponent = ProjectStoreComponent()
    override fun actionPerformed() {
        // make call to server to download a task archive
        val archiveFileLocation = httpDownloadTask.download(MainAction.HOST_TASKS_URL, pluginMainPopupWindow.taskId)

        // unzip downloaded task
        val unzippedLocation = unzipArchiveTask.unzip(archiveFileLocation)

        // refresh virtual file tree in intellij project
        virtualTreeRefreshTask.refresh(unzippedLocation)

        // save unzipped folder location to the project store
        projectStoreComponent.add(pluginMainPopupWindow.taskId, unzippedLocation)

        // try to remove archive
        val archiveDeleted = File(archiveFileLocation).delete()

//TODO support logging

//        if (!archiveDeleted) {
//            LOG.error("Can't delete archive: $archiveFileLocation")
//        }

        PluginMainPopupWindow.resultTextField.text = "Task successfully fetched as project $unzippedLocation. Do it!"
    }

}