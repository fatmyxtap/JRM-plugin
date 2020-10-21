package com.jrmplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.listener.CompleteTaskButtonListener;
import com.jrmplugin.listener.FetchTaskButtonListener;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.util.UiUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class MainAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(MainAction.class);
    public static Properties CORE_PROPERTIES = initServerSide();

    @NotNull
    private static Properties initServerSide() {
        try (InputStream input = MainAction.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info(String.format("Action (%s) has been started.", this.getClass().getName()));

        PluginMainPopupWindow pluginMainPopupWindow = new PluginMainPopupWindow(e.getProject(), UiUtil.createJPanel());

        pluginMainPopupWindow
                .createPopup()
                .showCenteredInCurrentWindow(Objects.requireNonNull(e.getProject()));

        pluginMainPopupWindow
                .getFetchTaskButton()
                .addMouseListener(new FetchTaskButtonListener(e, pluginMainPopupWindow));

        pluginMainPopupWindow
                .getCompleteTaskButton()
                .addMouseListener(new CompleteTaskButtonListener(e, pluginMainPopupWindow));

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
