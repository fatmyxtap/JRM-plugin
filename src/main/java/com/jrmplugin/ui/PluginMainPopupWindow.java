package com.jrmplugin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.popup.ComponentPopupBuilderImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PluginMainPopupWindow extends ComponentPopupBuilderImpl {

    private JPanel panel;

    private EditorTextField taskIdField;
    private PluginModalWindowButton fetchTaskButton;
    private Component completeTaskButton;

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

        this.taskIdField = new EditorTextField("Put task id here...");
        this.fetchTaskButton = new PluginModalWindowButton("Fetch Task From Server");
        this.completeTaskButton = new PluginModalWindowButton("Complete task");

        panel.add(taskIdField, BorderLayout.PAGE_START);
        panel.add(fetchTaskButton, BorderLayout.CENTER);
        panel.add(completeTaskButton, BorderLayout.AFTER_LAST_LINE);

        popup.setMinimumSize(new Dimension(400, 100));

        return popup;
    }

    public JPanel getPanel() {
        return panel;
    }

    public EditorTextField getTaskIdField() {
        return taskIdField;
    }

    public PluginModalWindowButton getFetchTaskButton() {
        return fetchTaskButton;
    }

    public Component getCompleteTaskButton() {
        return completeTaskButton;
    }
}
