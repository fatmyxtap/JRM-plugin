package com.jrmplugin.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.jrmplugin.core.ProcessableMouseAdapter;
import com.jrmplugin.core.ProjectStoreComponent;
import com.jrmplugin.core.VirtualTreeRefreshTask;
import com.jrmplugin.core.cache.TaskIdCache;
import com.jrmplugin.core.http.HttpUploadTask;
import com.jrmplugin.core.zip.ZipArchiveTask;
import com.jrmplugin.dto.ServerTaskResult;
import com.jrmplugin.ui.PluginMainPopupWindow;

import java.io.File;
import java.io.IOException;

public class CompleteTaskButtonListener extends ProcessableMouseAdapter {

    private final PluginMainPopupWindow pluginMainPopupWindow;

    private final ProjectStoreComponent projectStoreComponent;
    private final ZipArchiveTask zipArchiveTask;
    private final HttpUploadTask httpUploadTask;
    private final VirtualTreeRefreshTask virtualTreeRefreshTask;
    private final TaskIdCache taskCache = ServiceManager.getService(TaskIdCache.class);


    public CompleteTaskButtonListener(AnActionEvent event, PluginMainPopupWindow pluginMainPopupWindow) {
        super(pluginMainPopupWindow, pluginMainPopupWindow.getCompleteTaskButton());
        this.pluginMainPopupWindow = pluginMainPopupWindow;

        this.projectStoreComponent = new ProjectStoreComponent();
        this.zipArchiveTask = new ZipArchiveTask();
        this.httpUploadTask = new HttpUploadTask();
        this.virtualTreeRefreshTask = new VirtualTreeRefreshTask();
    }

    @Override
    protected void actionPerformed() {

        String taskSourcesLocation = taskCache.findById(pluginMainPopupWindow.getTaskId());

        // save unsaved documents in intellij project
        virtualTreeRefreshTask.save();

        File zipToSend = zipArchiveTask.zip(taskSourcesLocation);
        String result = this.parseResult(
                httpUploadTask.uploadFile(zipToSend, pluginMainPopupWindow.getTaskId())
        );
        pluginMainPopupWindow.getResultTextField().setText(result);
    }

    private String parseResult(String result) {
        if (result == null) {
            return "Server didn't answer on our request! Please, contact with admins.";
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ServerTaskResult serverTaskResult = objectMapper.readValue(result, ServerTaskResult.class);
                switch (serverTaskResult.getStatus()) {
                    case COMPLETED:
                        return "Completed! Go to next task!";
                    case UNCOMPLETED:
                        return "Some errors in your task, check please: " + serverTaskResult.getMessage();
                    case IN_PROGRESS:
                        return "Task in progress, please wait!";
                    case FAILURE:
                        return "Compilation error, check please error message: " + serverTaskResult.getMessage();
                    default:
                        return "Incorrect answer from server on our request! Please, contact with admins.";
                }
            } catch (IOException e) {
                return "Incorrect answer from server on our request! Please, contact with admins.";
            }
        }
    }

}
