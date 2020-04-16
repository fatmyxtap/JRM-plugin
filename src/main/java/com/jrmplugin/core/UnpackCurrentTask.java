package com.jrmplugin.core;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.util.Objects;

public class UnpackCurrentTask {

    public UnpackCurrentTask(AnActionEvent e, String source) {
        VirtualFile root = ModuleRootManager.getInstance(ModuleManager.getInstance(
                Objects.requireNonNull(e.getProject())).getModules()[0]).getContentRoots()[0];

        String destination = root.getPath();

        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (ZipException ex) {
            // some exception on unzipping
            System.err.println("OOPS....");
        }
    }

}
