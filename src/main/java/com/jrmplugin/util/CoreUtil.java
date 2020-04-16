package com.jrmplugin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ModuleRootManager;

import java.util.Objects;

public class CoreUtil {

    public static String getProjectFolder(AnActionEvent event) {
        return ModuleRootManager.getInstance(ModuleManager.getInstance(
                Objects.requireNonNull(event.getProject())).getModules()[0]).getContentRoots()[0].getPath();
    }

}
