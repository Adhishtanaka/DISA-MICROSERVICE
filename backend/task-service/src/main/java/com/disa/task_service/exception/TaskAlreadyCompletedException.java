/*
 * TaskAlreadyCompletedException is thrown when an operation is attempted on a task
 * that has already reached COMPLETED status (e.g., re-assigning a completed task).
 * Results in a 409 CONFLICT response when caught by the GlobalExceptionHandler.
 */
package com.disa.task_service.exception;

public class TaskAlreadyCompletedException extends RuntimeException {

    public TaskAlreadyCompletedException(Long id) {
        super("Task with id " + id + " is already completed");
    }

    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
