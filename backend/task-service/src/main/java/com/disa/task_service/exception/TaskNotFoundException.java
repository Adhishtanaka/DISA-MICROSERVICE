/*
 * TaskNotFoundException is thrown when a requested task does not exist in the system.
 * Results in a 404 NOT FOUND response when caught by the GlobalExceptionHandler.
 */
package com.disa.task_service.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
