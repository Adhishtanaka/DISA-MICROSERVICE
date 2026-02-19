/*
 * ErrorResponse is a structured DTO returned by the GlobalExceptionHandler
 * for all error conditions. Provides consistent error formatting across all endpoints.
 */
package com.disa.task_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
