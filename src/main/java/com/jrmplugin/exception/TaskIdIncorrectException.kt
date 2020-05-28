package com.jrmplugin.exception

class TaskIdIncorrectException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String, taskId: String) : super(message + taskId) {}

    companion object {
        const val TASK_ID_HAS_NOT_CHANGED = "Please put correct task id to the form."
        const val TASK_ID_IS_NOT_UUID = "Incorrect task id = "
    }
}