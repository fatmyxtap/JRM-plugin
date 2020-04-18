package com.jrmplugin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.popup.ComponentPopupBuilderImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class PluginMainPopupWindow extends ComponentPopupBuilderImpl {

    private JPanel panel;

    // this field is static,
    // because we want to save state across different calls between plugin execution
    private static EditorTextField taskIdField;

    private Component fetchTaskButton;
    private Component completeTaskButton;

    private final String defaultTaskIdFieldText = "Put task id here...";
    private final String defaultFetchTaskButtonText = "Fetch Task From Server";
    private final String defaultCompleteTaskButtonText = "Complete task";

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

        if (taskIdField == null)
            taskIdField = new EditorTextField(defaultTaskIdFieldText);

        this.fetchTaskButton = new PluginModalWindowButton(defaultFetchTaskButtonText);
        this.completeTaskButton = new PluginModalWindowButton(defaultCompleteTaskButtonText);

        panel.add(taskIdField, BorderLayout.PAGE_START);
        panel.add(fetchTaskButton, BorderLayout.CENTER);
        panel.add(completeTaskButton, BorderLayout.AFTER_LAST_LINE);

        popup.setMinimumSize(new Dimension(400, 100));

        return popup;
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getTaskId() {
        String text = taskIdField.getText();

        if (defaultTaskIdFieldText.equals(text)) {
            return null;
        }

        try {
            // just to check that the value is right uuid
            UUID.fromString(text);
        } catch (IllegalArgumentException ex) {
            return null;
        }

        return text;
    }

    public Component getFetchTaskButton() {
        return fetchTaskButton;
    }

    public Component getCompleteTaskButton() {
        return completeTaskButton;
    }
}
