package com.jrmplugin.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.roots.ModuleRootManager
import java.util.*

object CoreUtil {
    fun getProjectFolder(event: AnActionEvent): String {
        return ModuleRootManager.getInstance(
                ModuleManager.getInstance(event.project!!)!!.modules[0]
        ).contentRoots[0].path
    }
}