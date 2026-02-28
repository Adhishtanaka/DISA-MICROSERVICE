package com.example.personnel_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for all API exceptions.
 * Provides consistent error information to API clients.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * HTTP status reason phrase (e.g., "Not Found", "Bad Request").
     */
    private String error;

    /**
     * Detailed error message for developers.
     */
    private String message;

    /**
     * API endpoint path where the error occurred.
     */
    private String path;

    /**
     * List of validation errors (optional, used for validation failures).
     */
    private List<ValidationError> validationErrors;

    /**
     * Creates a simple error response without validation errors.
     * 
     * @param status HTTP status code
     * @param error HTTP status reason
     * @param message error message
     * @param path request path
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Inner class representing a single validation error.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidationError {
        /**
         * Field name that failed validation.
         */
        private String field;

        /**
         * Rejected value.
         */
        private Object rejectedValue;

        /**
         * Validation error message.
         */
        private String message;
    }
}
