package com.jrmplugin.core

import com.intellij.openapi.vfs.LocalFileSystem

class VirtualTreeRefreshTask {
    fun refresh(refreshFileLocation: String?) {
        LocalFileSystem.getInstance()
                .refreshAndFindFileByPath(refreshFileLocation!!)!!
                .refresh(true, true)
    }
}