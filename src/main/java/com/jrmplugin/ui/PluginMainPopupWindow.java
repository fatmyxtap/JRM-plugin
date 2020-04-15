package com.jrmplugin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.popup.ComponentPopupBuilderImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PluginMainPopupWindow extends ComponentPopupBuilderImpl {

    private JPanel panel;

    public PluginMainPopupWindow(JPanel panel) {
        super(panel, null);
        this.panel = panel;

        // default preferences
        setCancelOnClickOutside(true);
        setRequestFocus(true);
        setResizable(true);
        setMayBeParent(true);
    }

    @Override
    @NotNull
    public JBPopup createPopup() {
        JBPopup popup = super.createPopup();

        EditorTextField taskIdField = new EditorTextField("Put task id here...");
        Component fetchTask = new PluginModalWindowButton("Fetch Task From Server");
        Component completeTask = new PluginModalWindowButton("Complete task");

        panel.add(taskIdField, BorderLayout.PAGE_START);
        panel.add(fetchTask, BorderLayout.CENTER);
        panel.add(completeTask, BorderLayout.AFTER_LAST_LINE);

        popup.setMinimumSize(new Dimension(400, 100));

        return popup;
    }

}
