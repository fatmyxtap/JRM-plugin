package com.jrmplugin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBTextArea;
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
    private static JTextArea resultTextField;

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
        setCancelOnWindowDeactivation(false);
        setRequestFocus(true);
        setResizable(true);
        setMayBeParent(true);
    }

    @Override
    @NotNull
    public JBPopup createPopup() {
        JBPopup popup = super.createPopup();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        if (taskIdField == null)
            taskIdField = new EditorTextField(defaultTaskIdFieldText);
        panel.add(taskIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        this.fetchTaskButton = new PluginModalWindowButton(defaultFetchTaskButtonText);
        this.fetchTaskButton.setPreferredSize(new Dimension(250, 40));
        panel.add(fetchTaskButton, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        this.completeTaskButton = new PluginModalWindowButton(defaultCompleteTaskButtonText);
        this.completeTaskButton.setPreferredSize(new Dimension(200, 40));
        panel.add(completeTaskButton, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        if (resultTextField == null) {
            resultTextField = new JBTextArea(7, 50);
            resultTextField.setEditable(false);
            resultTextField.setLineWrap(true);
            resultTextField.setWrapStyleWord(true);
        }
        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(resultTextField);
        panel.add(scrollPane, c);

        popup.setMinimumSize(new Dimension(400, 100));
        panel.setMinimumSize(new Dimension(400, 100));

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

    public JTextArea getResultTextField() {
        return resultTextField;
    }

}
