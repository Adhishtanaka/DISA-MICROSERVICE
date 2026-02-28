package com.example.personnel_service.exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 * This is typically used when querying by ID or other unique identifiers.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Constructs a new ResourceNotFoundException with a custom message.
     * 
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with resource details.
     * Creates a formatted message: "[ResourceName] not found with [fieldName]: [fieldValue]"
     * 
     * @param resourceName the name of the resource (e.g., "Person", "Skill")
     * @param fieldName the field used for lookup (e.g., "id", "email")
     * @param fieldValue the value that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
