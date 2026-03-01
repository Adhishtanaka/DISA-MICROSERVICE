/**
 * Custom exception thrown when a requested resource is not found.
 * Extends RuntimeException for unchecked exception handling.
 */
package com.disa.resource_service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}