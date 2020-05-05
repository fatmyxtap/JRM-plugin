package com.jrmplugin.exception;

public class TaskByIdNotFoundException extends RuntimeException {

    public TaskByIdNotFoundException(String message) {
        super(message);
    }
}
