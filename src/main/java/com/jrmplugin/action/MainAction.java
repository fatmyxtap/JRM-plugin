package com.jrmplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.EditorTextField;
import com.jrmplugin.core.HttpDownloadUtil;
import com.jrmplugin.core.UnpackCurrentTask;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.ui.util.UiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MainAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(MainAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info(String.format("Action (%s) has been started.", this.getClass().getName()));

        // all logic here

        PluginMainPopupWindow pluginMainPopupWindow = new PluginMainPopupWindow(UiUtil.createJPanel());
        pluginMainPopupWindow.createPopup().showInBestPositionFor(e.getDataContext());

        String hostUrl = "http://localhost:8081/tasks/";

        pluginMainPopupWindow
                .getFetchTaskButton()
                .addActionListener((actionEvent) -> {
                    EditorTextField taskIdField = pluginMainPopupWindow.getTaskIdField();

                    try {
                        // just to check that the value is right uuid
                        UUID.fromString(taskIdField.getText());
                    } catch (IllegalArgumentException ex) {
                        // don't do anything because task id incorrect
                    }

                    // make call to server for download a task
                    String archiveFileLocation
                            = HttpDownloadUtil.downloadFile(e, hostUrl + taskIdField.getText());

                    if (archiveFileLocation == null) {
                        // don't find file
                        throw new IllegalArgumentException("Incorrect TASK ID!!");
                    }

                    new UnpackCurrentTask(e, archiveFileLocation);
                });
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

    @Override
    public boolean useSmallerFontForTextInToolbar() {
        return true;
    }
}
