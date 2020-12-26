package com.jrmplugin.core;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.LocalFileSystem;

import java.util.Objects;

public class VirtualTreeRefreshTask {

    public void refresh(String refreshFileLocation, boolean async) {
        Objects.requireNonNull(LocalFileSystem.getInstance()
                .refreshAndFindFileByPath(refreshFileLocation))
                .refresh(async, true);
    }

    public void save() {
        final Application app = ApplicationManager.getApplication();
        Runnable action = () ->
                app.runWriteAction(
                        () -> {
                            FileDocumentManager.getInstance().saveAllDocuments();
                        });
        if (app.isDispatchThread()) {
            action.run();
        } else {
            app.invokeAndWait(action, ModalityState.defaultModalityState());
        }
    }

}
