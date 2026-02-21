package com.example.personnel_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the Personnel Service API.
 * Catches and handles all exceptions thrown by controllers, providing consistent
 * error responses to API clients.
 * 
 * <p>This handler provides specific handling for:</p>
 * <ul>
 *   <li>Resource not found exceptions (404)</li>
 *   <li>Bad request exceptions (400)</li>
 *   <li>Duplicate resource exceptions (409)</li>
 *   <li>Validation errors (400)</li>
 *   <li>Type mismatch errors (400)</li>
 *   <li>Malformed JSON errors (400)</li>
 *   <li>General runtime exceptions (500)</li>
 * </ul>
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException when a requested resource is not found.
     * Returns HTTP 404 (Not Found).
     * 
     * @param ex the ResourceNotFoundException
     * @param request the HTTP request
     * @return ResponseEntity with error details and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException for invalid client requests.
     * Returns HTTP 400 (Bad Request).
     * 
     * @param ex the BadRequestException
     * @param request the HTTP request
     * @return ResponseEntity with error details and 400 status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DuplicateResourceException when attempting to create a resource that already exists.
     * Returns HTTP 409 (Conflict).
     * 
     * @param ex the DuplicateResourceException
     * @param request the HTTP request
     * @return ResponseEntity with error details and 409 status
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles validation errors from @Valid annotations.
     * Returns HTTP 400 (Bad Request) with detailed field-level validation errors.
     * 
     * @param ex the MethodArgumentNotValidException
     * @param request the HTTP request
     * @return ResponseEntity with validation error details and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            String errorMessage = error.getDefaultMessage();
            validationErrors.add(new ErrorResponse.ValidationError(fieldName, rejectedValue, errorMessage));
        });
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(java.time.LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Validation failed for one or more fields");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setValidationErrors(validationErrors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles type mismatch errors (e.g., passing string where number is expected).
     * Returns HTTP 400 (Bad Request).
     * 
     * @param ex the MethodArgumentTypeMismatchException
     * @param request the HTTP request
     * @return ResponseEntity with error details and 400 status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles malformed JSON or unreadable request body errors.
     * Returns HTTP 400 (Bad Request).
     * 
     * @param ex the HttpMessageNotReadableException
     * @param request the HTTP request
     * @return ResponseEntity with error details and 400 status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed JSON request or invalid request body",
                request.getRequestURI()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other uncaught exceptions.
     * Returns HTTP 500 (Internal Server Error).
     * 
     * @param ex the Exception
     * @param request the HTTP request
     * @return ResponseEntity with error details and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred: " + ex.getMessage(),
                request.getRequestURI()
        );
        
        // Log the exception for debugging (in production, use proper logging framework)
        ex.printStackTrace();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
