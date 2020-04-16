package com.jrmplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.popup.JBPopup;
import com.jrmplugin.core.UnpackCurrentTask;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.ui.util.UiUtil;
import org.jetbrains.annotations.NotNull;

public class MainAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(MainAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info(String.format("Action (%s) has been started.", this.getClass().getName()));

        // all logic here

        JBPopup popup = new PluginMainPopupWindow(UiUtil.createJPanel()).createPopup();
        popup.showInBestPositionFor(e.getDataContext());

        new UnpackCurrentTask(e, "/home/myxtap/Projects/java/mock-project.zip");
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
