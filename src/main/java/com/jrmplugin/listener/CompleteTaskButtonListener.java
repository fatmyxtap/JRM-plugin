package com.jrmplugin.listener;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.core.ProjectStoreComponent;
import com.jrmplugin.core.ZipArchiveTask;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Supplier;

public class CompleteTaskButtonListener implements MouseListener {

    private static final Logger LOG = Logger.getInstance(CompleteTaskButtonListener.class);

    private final Supplier<String> taskIdFieldReader;
    private final ProjectStoreComponent projectStoreComponent;
    private final ZipArchiveTask zipArchiveTask;

    public CompleteTaskButtonListener(AnActionEvent event, Supplier<String> taskIdFieldReader) {
        this.taskIdFieldReader = taskIdFieldReader;
        this.projectStoreComponent = new ProjectStoreComponent();
        this.zipArchiveTask = new ZipArchiveTask();
    }

    public void actionPerformed(MouseEvent mouseEvent) {
        String taskSourcesLocation = projectStoreComponent.get(taskIdFieldReader.get());
        zipArchiveTask.zip(taskSourcesLocation);
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
