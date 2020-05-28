package com.jrmplugin.core

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.jrmplugin.ui.PluginMainPopupWindow
import com.jrmplugin.ui.PluginModalWindowProcessableButton
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

abstract class ProcessableMouseAdapter protected constructor(private val processableButton: PluginModalWindowProcessableButton) : MouseAdapter() {
    override fun mouseClicked(mouseEvent: MouseEvent) {
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                processableButton.setProgress(true)
                actionPerformed()
            } catch (ex: RuntimeException) {
                LOG.error(ex)
                PluginMainPopupWindow.resultTextField.text = ex.message
            } finally {
                processableButton.setProgress(false)
            }
        }
    }

    protected abstract fun actionPerformed()

    companion object {
        @JvmField
        protected val LOG = Logger.getInstance(ProcessableMouseAdapter::class.java)
    }

}