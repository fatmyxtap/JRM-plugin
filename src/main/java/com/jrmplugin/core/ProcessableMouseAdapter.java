package com.jrmplugin.core;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.jrmplugin.ui.PluginMainPopupWindow;
import com.jrmplugin.ui.PluginModalWindowProcessableButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ProcessableMouseAdapter extends MouseAdapter {

    protected static final Logger LOG = Logger.getInstance(ProcessableMouseAdapter.class);

    private final PluginMainPopupWindow pluginMainPopupWindow;
    private final PluginModalWindowProcessableButton processableButton;

    protected ProcessableMouseAdapter(PluginMainPopupWindow pluginMainPopupWindow,
                                      PluginModalWindowProcessableButton processableButton) {
        this.pluginMainPopupWindow = pluginMainPopupWindow;
        this.processableButton = processableButton;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                processableButton.setProgress(true);
                actionPerformed();
            } catch (RuntimeException ex) {
                LOG.error(ex);
                pluginMainPopupWindow.getResultTextField().setText(ex.getMessage());
            } finally {
                processableButton.setProgress(false);
            }
        });
    }

    protected abstract void actionPerformed();

}
