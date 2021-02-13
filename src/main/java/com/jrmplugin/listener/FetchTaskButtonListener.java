package com.jrmplugin.listener;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.jrmplugin.core.ProcessableMouseAdapter;
import com.jrmplugin.core.ProjectStoreComponent;
import com.jrmplugin.core.VirtualTreeRefreshTask;
import com.jrmplugin.core.cache.TaskIdCache;
import com.jrmplugin.core.http.HttpDownloadTask;
import com.jrmplugin.core.zip.UnzipArchiveTask;
import com.jrmplugin.dto.Task;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.util.CoreUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static com.jrmplugin.action.MainAction.CORE_PROPERTIES;
import static java.nio.file.Files.delete;
import static java.nio.file.Paths.get;

public class FetchTaskButtonListener extends ProcessableMouseAdapter {

    private final PluginMainPopupWindow pluginMainPopupWindow;

    private final HttpDownloadTask httpDownloadTask;
    private final UnzipArchiveTask unzipArchiveTask;
    private final VirtualTreeRefreshTask virtualTreeRefreshTask;
    private final ProjectStoreComponent projectStoreComponent;
    TaskIdCache cache = ServiceManager.getService(TaskIdCache.class);


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
        String archiveFileLocation = httpDownloadTask.download(
                CORE_PROPERTIES.getProperty("api.binaryTaskUrl"), pluginMainPopupWindow.getTaskId());

        // unzip downloaded task
        String unzippedLocation = unzipArchiveTask.unzip(archiveFileLocation);

        // refresh virtual file tree in intellij project
        virtualTreeRefreshTask.refresh(unzippedLocation, true);

        // save unzipped folder location to the project store
        projectStoreComponent.add(pluginMainPopupWindow.getTaskId(), unzippedLocation);

        // try to remove archive
        try {
            delete(get(archiveFileLocation));
        } catch (IOException e) {
            LOG.error("Can't delete archive: " + archiveFileLocation);
        }
        cache.putNewTask(new Task(pluginMainPopupWindow.getTaskId(), unzippedLocation));
        pluginMainPopupWindow.getResultTextField().setText("Task successfully fetched as project " + unzippedLocation + ". Do it!");
    }

}
