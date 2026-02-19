/*
 * GlobalExceptionHandler centralizes error handling for all controllers in the task service.
 * Maps domain-specific exceptions to appropriate HTTP status codes with structured
 * ErrorResponse bodies, ensuring consistent API error formatting across all endpoints.
 */
package com.disa.task_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link TaskNotFoundException} and returns a 404 Not Found response.
     *
     * @param e the exception thrown when a task lookup fails
     * @return structured 404 error response
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException e) {
        log.warn("Task not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Not Found", e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Handles {@link TaskAlreadyCompletedException} and returns a 409 Conflict response.
     *
     * @param e the exception thrown when an operation targets a completed task
     * @return structured 409 error response
     */
    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleTaskAlreadyCompleted(TaskAlreadyCompletedException e) {
        log.warn("Task already completed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Conflict", e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Handles {@link InvalidTaskStateException} and returns a 422 Unprocessable Entity response.
     *
     * @param e the exception thrown when a transition is invalid for the task's current state
     * @return structured 422 error response
     */
    @ExceptionHandler(InvalidTaskStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTaskState(InvalidTaskStateException e) {
        log.warn("Invalid task state: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(422, "Unprocessable Entity", e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Catch-all handler for unrecognised {@link RuntimeException} types.
     * Returns a 400 Bad Request response.
     *
     * @param e the uncaught runtime exception
     * @return structured 400 error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("Unexpected runtime error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Bad Request", e.getMessage(), LocalDateTime.now()));
    }

    /**
     * Last-resort handler for any {@link Exception} not matched by a more specific handler.
     * Returns a 500 Internal Server Error response.
     *
     * @param e the unhandled exception
     * @return structured 500 error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Internal Server Error", "An unexpected error occurred", LocalDateTime.now()));
    }
}