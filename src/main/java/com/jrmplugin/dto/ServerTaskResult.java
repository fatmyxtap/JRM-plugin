package com.jrmplugin.dto;

public class ServerTaskResult {

    private Status status = Status.IN_PROGRESS;
    private String message;

    public enum Status {
        /**
         * Tests failed with some response message
         */
        UNCOMPLETED,
        /**
         * Tests passed
         */
        COMPLETED,
        /**
         * Compilation error or build failure
         */
        FAILURE,
        /**
         * Tests in progress
         */
        IN_PROGRESS,
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
