package com.jrmplugin.core

import com.intellij.ide.util.PropertiesComponent

class ProjectStoreComponent {
    private val store: PropertiesComponent = PropertiesComponent.getInstance()
    fun add(key: String, value: String) {
        store.setValue(key, value)
    }

    operator fun get(key: String): String? {
        return store.getValue(key)
    }

}