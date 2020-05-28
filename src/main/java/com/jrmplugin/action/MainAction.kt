package com.jrmplugin.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.jrmplugin.listener.CompleteTaskButtonListener
import com.jrmplugin.listener.FetchTaskButtonListener
import com.jrmplugin.ui.PluginMainPopupWindow
import com.jrmplugin.util.UiUtil

class MainAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        LOG.info(String.format("Action (%s) has been started.", this.javaClass.name))

        with(PluginMainPopupWindow(e.project!!, UiUtil.createJPanel())) {
            this.createPopup().showCenteredInCurrentWindow(e.project!!)
            PluginMainPopupWindow.fetchTaskButton.addMouseListener(FetchTaskButtonListener(e, this))
            PluginMainPopupWindow.completeTaskButton.addMouseListener(CompleteTaskButtonListener(this))
        }
    }

    override fun displayTextInToolbar(): Boolean = true

    override fun useSmallerFontForTextInToolbar(): Boolean = true

    companion object {
        private val LOG = Logger.getInstance(MainAction::class.java)

        @JvmField
        val HOST_TASKS_URL = initServerSide()
        private fun initServerSide(): String {
            return when {
                System.getenv("local") != null -> "http://localhost:8081/tasks/"
                else -> "http://javaroadmap.ru:8081/tasks/"
            }
        }
    }
}