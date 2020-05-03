package com.jrmplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.listener.CompleteTaskButtonListener;
import com.jrmplugin.listener.FetchTaskButtonListener;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.util.UiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(MainAction.class);
    public static final String HOST_TASKS_URL = "http://localhost:8081/tasks/";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info(String.format("Action (%s) has been started.", this.getClass().getName()));

        PluginMainPopupWindow pluginMainPopupWindow = new PluginMainPopupWindow(UiUtil.createJPanel());

        pluginMainPopupWindow
                .createPopup()
                .showCenteredInCurrentWindow(Objects.requireNonNull(e.getProject()));

        pluginMainPopupWindow
                .getFetchTaskButton()
                .addMouseListener(new FetchTaskButtonListener(e, pluginMainPopupWindow::getTaskId));

        pluginMainPopupWindow
                .getCompleteTaskButton()
                .addMouseListener(new CompleteTaskButtonListener(e, pluginMainPopupWindow, pluginMainPopupWindow::getTaskId));
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
