package com.jrmplugin.listener;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.core.HttpDownloadTask;
import com.jrmplugin.core.UnzipArchiveTask;
import com.jrmplugin.util.CoreUtil;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.function.Supplier;

import static com.jrmplugin.action.MainAction.HOST_TASKS_URL;

public class FetchTaskButtonListener implements MouseListener {

    private static final Logger LOG = Logger.getInstance(FetchTaskButtonListener.class);

    private final Supplier<String> taskIdFieldReader;
    private final HttpDownloadTask httpDownloadTask;
    private final UnzipArchiveTask unzipArchiveTask;

    public FetchTaskButtonListener(AnActionEvent event, Supplier<String> taskIdFieldReader) {
        this.taskIdFieldReader = taskIdFieldReader;
        this.httpDownloadTask = new HttpDownloadTask(CoreUtil.getProjectFolder(event));
        this.unzipArchiveTask = new UnzipArchiveTask(CoreUtil.getProjectFolder(event));
    }

    public void actionPerformed(MouseEvent mouseEvent) {
        // make call to server to download a task archive
        String archiveFileLocation = httpDownloadTask.download(HOST_TASKS_URL + taskIdFieldReader.get());
        if (archiveFileLocation == null) {
            LOG.error("Can't download task from server: " + taskIdFieldReader.get());
            throw new IllegalArgumentException("Can't download task from server: " + taskIdFieldReader.get());
        }

        // unzip downloaded task
        boolean unzipped = unzipArchiveTask.unzip(archiveFileLocation);
        if (!unzipped) {
            LOG.error("Can't unzip file: " + archiveFileLocation);
            throw new IllegalStateException("Can't unzip file: " + archiveFileLocation);
        }

        // try to remove archive
        boolean archiveDeleted = new File(archiveFileLocation).delete();
        if (!archiveDeleted) {
            LOG.error("Can't delete archive: " + archiveFileLocation);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        actionPerformed(mouseEvent);
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
