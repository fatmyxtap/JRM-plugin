package com.jrmplugin.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.core.ProjectStoreComponent;
import com.jrmplugin.core.http.HttpUploadTask;
import com.jrmplugin.core.zip.ZipArchiveTask;
import com.jrmplugin.dto.ServerTaskResult;
import com.jrmplugin.ui.PluginMainPopupWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class CompleteTaskButtonListener implements MouseListener {

    private static final Logger LOG = Logger.getInstance(CompleteTaskButtonListener.class);

    private final Supplier<String> taskIdFieldReader;
    private final ProjectStoreComponent projectStoreComponent;
    private final ZipArchiveTask zipArchiveTask;
    private final HttpUploadTask httpUploadTask;
    private final PluginMainPopupWindow pluginMainPopupWindow;

    public CompleteTaskButtonListener(AnActionEvent event, PluginMainPopupWindow pluginMainPopupWindow, Supplier<String> taskIdFieldReader) {
        this.taskIdFieldReader = taskIdFieldReader;
        this.projectStoreComponent = new ProjectStoreComponent();
        this.zipArchiveTask = new ZipArchiveTask();
        this.httpUploadTask = new HttpUploadTask();
        this.pluginMainPopupWindow = pluginMainPopupWindow;
    }

    public void actionPerformed(MouseEvent mouseEvent) {
        String taskSourcesLocation = projectStoreComponent.get(taskIdFieldReader.get());
        File zipToSend = zipArchiveTask.zip(taskSourcesLocation);

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    pluginMainPopupWindow.setInProgress(true);
                    String result = httpUploadTask.uploadFile(zipToSend, taskIdFieldReader.get());
                    this.updateResultField(result);
                    pluginMainPopupWindow.setInProgress(false);
                }
        );
    }

    private void updateResultField(String result) {
        if (result == null) {
            pluginMainPopupWindow.getResultTextField().setText("Server didn't answer on our request! Please, contact with admins.");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ServerTaskResult serverTaskResult
                        = objectMapper.readValue(result, ServerTaskResult.class);
                switch (serverTaskResult.getStatus()) {
                    case COMPLETED:
                        pluginMainPopupWindow.getResultTextField().setText("Completed! Go to next task!");
                        break;
                    case UNCOMPLETED:
                        pluginMainPopupWindow.getResultTextField().setText("Some errors in your task, check please: " + serverTaskResult.getMessage());
                        break;
                    case IN_PROGRESS:
                        pluginMainPopupWindow.getResultTextField().setText("Task in progress, please wait!");
                        break;
                    case FAILURE:
                        pluginMainPopupWindow.getResultTextField().setText("Compilation error, check please error message: " + serverTaskResult.getMessage());
                        break;
                    default:
                        pluginMainPopupWindow.getResultTextField().setText("Server answer on our request incorrectly! Please, contact with admins.");
                }
            } catch (IOException e) {
                pluginMainPopupWindow.getResultTextField().setText("Server answer on our request incorrectly! Please, contact with admins.");
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        this.actionPerformed(mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
