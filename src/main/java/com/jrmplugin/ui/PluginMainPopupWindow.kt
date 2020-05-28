package com.jrmplugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.ui.EditorTextField
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.popup.ComponentPopupBuilderImpl
import com.jrmplugin.core.SafeJTextArea
import com.jrmplugin.exception.TaskIdIncorrectException
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.util.*
import javax.swing.JPanel

class PluginMainPopupWindow(val project: Project, val panel: JPanel) : ComponentPopupBuilderImpl(panel, null) {
    companion object {
        private const val defaultFetchTaskButtonText = "Fetch Task"
        private const val defaultTaskIdFieldText = "Put task id here..."
        private const val defaultCompleteTaskButtonText = "Complete Task"

        // this field is static,
        // because we want to save state across different calls between plugin execution
        private var taskIdField = EditorTextField(defaultTaskIdFieldText)
        var resultTextField = SafeJTextArea(7, 50).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
        }
        var fetchTaskButton = PluginModalWindowProcessableButton(defaultFetchTaskButtonText)
        var completeTaskButton = PluginModalWindowProcessableButton(defaultCompleteTaskButtonText)
    }

    init {
        // default preferences
        setCancelOnClickOutside(true)
        setCancelOnWindowDeactivation(false)
        setRequestFocus(true)
        setResizable(true)
        setMayBeParent(true)
    }

    override fun createPopup(): JBPopup {
        val popup = super.createPopup().apply {
            setMinimumSize(Dimension(400, 100))
        }

        panel.add(taskIdField, createGrid(gridx = 0, gridy = 0, gridwidth = 2))

        with(createGrid(gridx = 0, gridy = 1, gridwidth = 1)) {
            panel.add(fetchTaskButton, this)
            panel.add(fetchTaskButton.progressBar, this)
        }

        with(createGrid(gridx = 1, gridy = 1, gridwidth = 1)) {
            panel.add(completeTaskButton, this)
            panel.add(completeTaskButton.progressBar, this)
        }

        with(createGrid(gridx = 0, gridy = 2, gridwidth = 2)) {
            val scrollPane = ScrollPaneFactory.createScrollPane(resultTextField)
            panel.add(scrollPane, this)
        }

        panel.minimumSize = Dimension(400, 100)
        return popup
    }

    private fun createGrid(gridx: Int, gridy: Int, gridwidth: Int): GridBagConstraints = GridBagConstraints().apply {
        fill = GridBagConstraints.HORIZONTAL
        this.gridx = gridx
        this.gridy = gridy
        this.gridwidth = gridwidth
    }

    // just to check that the value is right uuid
    val taskId: String
        get() {
            val text = taskIdField.text
            if (defaultTaskIdFieldText == text) {
                throw TaskIdIncorrectException(TaskIdIncorrectException.TASK_ID_HAS_NOT_CHANGED)
            }
            try {
                // just to check that the value is right uuid
                UUID.fromString(text)
            } catch (ex: IllegalArgumentException) {
                throw TaskIdIncorrectException(TaskIdIncorrectException.TASK_ID_IS_NOT_UUID, taskIdField.text)
            }
            return text
        }

}