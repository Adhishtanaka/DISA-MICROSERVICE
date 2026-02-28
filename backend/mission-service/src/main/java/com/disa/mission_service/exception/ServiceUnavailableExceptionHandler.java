package com.disa.mission_service.exception;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles exceptions that occur when a microservice or its dependencies
 * (database, message broker, external service) become unavailable.
 *
 * Extends {@link GlobalExceptionHandler} to inherit base exception handling
 * while adding service-failure-specific responses.
 */
@RestControllerAdvice
public class ServiceUnavailableExceptionHandler extends GlobalExceptionHandler {

    /**
     * Handles the custom {@link ServiceUnavailableException} thrown explicitly
     * when a dependent service is detected as down.
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleServiceUnavailable(ServiceUnavailableException ex) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service Unavailable",
                ex.getServiceName() + " is currently unavailable: " + ex.getMessage()
        );
    }

    /**
     * Handles database connectivity failures (e.g., PostgreSQL is down).
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Database Unavailable",
                "The database is currently unreachable. Please try again later."
        );
    }

    /**
     * Handles RabbitMQ broker connection failures.
     */
    @ExceptionHandler({AmqpConnectException.class, AmqpIOException.class})
    public ResponseEntity<Map<String, Object>> handleAmqpException(Exception ex) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Message Broker Unavailable",
                "The message broker (RabbitMQ) is currently unreachable. Please try again later."
        );
    }

    /**
     * Handles low-level TCP/IP connection refused errors to any downstream service.
     */
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Map<String, Object>> handleConnectException(ConnectException ex) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Connection Refused",
                "A downstream service refused the connection. Please try again later."
        );
    }

    /**
     * Catch-all for any unexpected errors that crash the service entirely.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please contact the system administrator."
        );
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("service", "mission-service");
        return ResponseEntity.status(status).body(body);
    }
}
