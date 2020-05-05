package com.jrmplugin.exception;

public class TaskIdIncorrectException extends RuntimeException {

    public static final String TASK_ID_HAS_NOT_CHANGED = "Please put correct task id to the form.";
    public static final String TASK_ID_IS_NOT_UUID = "Incorrect task id = ";

    public TaskIdIncorrectException(String message) {
        super(message);
    }

    public TaskIdIncorrectException(String message, String taskId) {
        super(message + taskId);
    }

}
