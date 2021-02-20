package com.jrmplugin.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskHolder {

    List<Task> tasks = new ArrayList<>();


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void putNewTask(Task task) {
        tasks = tasks.stream().filter(t -> !t.getTaskId().equals(task.getTaskId())).collect(Collectors.toList());
        tasks.add(task);
    }


}
