package com.example.personnel_service.exception;

/**
 * Exception thrown when the client sends an invalid request.
 * This includes validation errors, malformed data, or business rule violations.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     * 
     * @param message the detail message explaining why the request is invalid
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new BadRequestException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
