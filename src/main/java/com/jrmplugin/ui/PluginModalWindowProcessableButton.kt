package com.jrmplugin.ui

import java.awt.Dimension
import javax.swing.JProgressBar

class PluginModalWindowProcessableButton(text: String) : PluginModalWindowButton(text) {
    val progressBar: JProgressBar = JProgressBar().apply {
        isIndeterminate = true
        toolTipText = "Wait please."
        preferredSize = Dimension(200, 15)
        isVisible = false
    }

    fun setProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.isVisible = true
            this.isVisible = false
        } else {
            progressBar.isVisible = false
            this.isVisible = true
        }
    }
}