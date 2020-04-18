package com.jrmplugin.core;

import com.intellij.ide.util.PropertiesComponent;

public class ProjectStoreComponent {

    private final PropertiesComponent store;

    public ProjectStoreComponent() {
        this.store = PropertiesComponent.getInstance();
    }

    public void add(String key, String value) {
        store.setValue(key, value);
    }

    public String get(String key) {
        return store.getValue(key);
    }

}
