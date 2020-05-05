package com.jrmplugin.listener;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jrmplugin.core.ProcessableMouseAdapter;
import com.jrmplugin.core.ProjectStoreComponent;
import com.jrmplugin.core.VirtualTreeRefreshTask;
import com.jrmplugin.core.http.HttpDownloadTask;
import com.jrmplugin.core.zip.UnzipArchiveTask;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.util.CoreUtil;

import java.io.File;

import static com.jrmplugin.action.MainAction.HOST_TASKS_URL;

public class FetchTaskButtonListener extends ProcessableMouseAdapter {

    private final PluginMainPopupWindow pluginMainPopupWindow;

    private final HttpDownloadTask httpDownloadTask;
    private final UnzipArchiveTask unzipArchiveTask;
    private final VirtualTreeRefreshTask virtualTreeRefreshTask;
    private final ProjectStoreComponent projectStoreComponent;

    public FetchTaskButtonListener(AnActionEvent event, PluginMainPopupWindow pluginMainPopupWindow) {
        super(pluginMainPopupWindow, pluginMainPopupWindow.getFetchTaskButton());
        this.pluginMainPopupWindow = pluginMainPopupWindow;

        this.httpDownloadTask = new HttpDownloadTask(CoreUtil.getProjectFolder(event));
        this.unzipArchiveTask = new UnzipArchiveTask(CoreUtil.getProjectFolder(event));
        this.virtualTreeRefreshTask = new VirtualTreeRefreshTask();
        this.projectStoreComponent = new ProjectStoreComponent();
    }

    @Override
    protected void actionPerformed() {
        // make call to server to download a task archive
        String archiveFileLocation = httpDownloadTask.download(HOST_TASKS_URL, pluginMainPopupWindow.getTaskId());

        // unzip downloaded task
        String unzippedLocation = unzipArchiveTask.unzip(archiveFileLocation);

        // refresh virtual file tree in intellij project
        virtualTreeRefreshTask.refresh(unzippedLocation);

        // save unzipped folder location to the project store
        projectStoreComponent.add(pluginMainPopupWindow.getTaskId(), unzippedLocation);

        // try to remove archive
        boolean archiveDeleted = new File(archiveFileLocation).delete();
        if (!archiveDeleted) {
            LOG.error("Can't delete archive: " + archiveFileLocation);
        }

        pluginMainPopupWindow.getResultTextField().setText("Task successfully fetched as project " + unzippedLocation + ". Do it!");
    }

}
