package com.jrmplugin.dto

class ServerTaskResult {
    val status = Status.IN_PROGRESS
    val message: String? = null

    enum class Status {
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
        IN_PROGRESS
    }
}