package com.jrmplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.popup.ComponentPopupBuilderImpl;
import com.jrmplugin.core.SafeJTextArea;
import com.jrmplugin.exception.TaskIdIncorrectException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

import static com.jrmplugin.exception.TaskIdIncorrectException.TASK_ID_HAS_NOT_CHANGED;
import static com.jrmplugin.exception.TaskIdIncorrectException.TASK_ID_IS_NOT_UUID;

public class PluginMainPopupWindow extends ComponentPopupBuilderImpl {

    private final JPanel panel;
    private final Project project;

    // this field is static,
    // because we want to save state across different calls between plugin execution
    private static EditorTextField taskIdField;
    private static SafeJTextArea resultTextField;
    private static PluginModalWindowProcessableButton fetchTaskButton;
    private static PluginModalWindowProcessableButton completeTaskButton;


    private final String defaultTaskIdFieldText = "Put task id here...";
    private final String defaultFetchTaskButtonText = "Fetch Task";
    private final String defaultCompleteTaskButtonText = "Complete Task";

    public PluginMainPopupWindow(Project project, JPanel panel) {
        super(panel, null);
        this.panel = panel;
        this.project = project;

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
        if (taskIdField == null) {
            taskIdField = new EditorTextField(defaultTaskIdFieldText);
        }
        panel.add(taskIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        if (fetchTaskButton == null) {
            fetchTaskButton = new PluginModalWindowProcessableButton(defaultFetchTaskButtonText);
        }
        panel.add(fetchTaskButton, c);
        panel.add(fetchTaskButton.getProgressBar(), c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        if (completeTaskButton == null) {
            completeTaskButton = new PluginModalWindowProcessableButton(defaultCompleteTaskButtonText);
        }
        panel.add(completeTaskButton, c);
        panel.add(completeTaskButton.getProgressBar(), c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        if (resultTextField == null) {
            resultTextField = new SafeJTextArea(7, 50);
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

    public Project getProject() {
        return project;
    }

    public String getTaskId() {
        String text = taskIdField.getText();

        if (defaultTaskIdFieldText.equals(text)) {
            throw new TaskIdIncorrectException(TASK_ID_HAS_NOT_CHANGED);
        }

        try {
            // just to check that the value is right uuid
            UUID.fromString(text);
        } catch (IllegalArgumentException ex) {
            throw new TaskIdIncorrectException(TASK_ID_IS_NOT_UUID, taskIdField.getText());
        }

        return text;
    }

    public PluginModalWindowProcessableButton getFetchTaskButton() {
        return fetchTaskButton;
    }

    public PluginModalWindowProcessableButton getCompleteTaskButton() {
        return completeTaskButton;
    }

    public JTextArea getResultTextField() {
        return resultTextField;
    }

}
