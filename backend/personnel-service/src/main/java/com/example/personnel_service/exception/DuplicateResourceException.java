package com.example.personnel_service.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * Typically used when unique constraints are violated.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     * 
     * @param message the detail message explaining the duplicate resource
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateResourceException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
