package com.jrmplugin.core;

import com.intellij.openapi.vfs.LocalFileSystem;

import java.util.Objects;

public class VirtualTreeRefreshTask {

    public void refresh(String refreshFileLocation) {
        Objects.requireNonNull(LocalFileSystem.getInstance()
                .refreshAndFindFileByPath(refreshFileLocation))
                .refresh(true, true);
    }

}
