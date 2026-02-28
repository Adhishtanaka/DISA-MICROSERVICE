/*
 * InvalidTaskStateException is thrown when a task operation is invalid for the
 * task's current state (e.g., completing a task that hasn't been assigned yet).
 * Results in a 422 UNPROCESSABLE ENTITY response when caught by GlobalExceptionHandler.
 */
package com.disa.task_service.exception;

public class InvalidTaskStateException extends RuntimeException {

    public InvalidTaskStateException(String message) {
        super(message);
    }
}
