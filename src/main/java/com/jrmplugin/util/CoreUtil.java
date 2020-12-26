package com.jrmplugin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Objects;

public class CoreUtil {

    public static String getProjectFolder(AnActionEvent event) {
        return Objects.requireNonNull(event.getProject()).getBasePath();
    }

}
