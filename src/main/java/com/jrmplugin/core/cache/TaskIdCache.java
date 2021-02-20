package com.jrmplugin.core.cache;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.jrmplugin.dto.Task;
import com.jrmplugin.dto.TaskHolder;
import com.jrmplugin.exception.TaskByIdNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "jrm-tasks", storages = {
        @Storage(value = "$APP_CONFIG$/jrm-tasks.xml")
})
public class TaskIdCache implements PersistentStateComponent<TaskHolder> {

    public TaskHolder tasks = new TaskHolder();

    @Nullable
    @Override
    public TaskHolder getState() {
        return tasks;
    }

    @Override
    public void loadState(@NotNull TaskHolder state) {
        XmlSerializerUtil.copyBean(state, tasks);
    }

    @Override
    public void noStateLoaded() {}

    @Override
    public void initializeComponent() {}


    public void putNewTask(Task task) {
        tasks.putNewTask(task);
    }

    public String findById(String taskId) {
        return tasks.getTasks().stream().filter(s -> s.getTaskId().equals(taskId)).map(Task::getTaskName).findFirst().orElseThrow(() -> new TaskByIdNotFoundException(taskId));
    }
}
