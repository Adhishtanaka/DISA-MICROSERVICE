/*
 * AssignTaskRequest DTO carrying the personnel ID to whom a task should be assigned.
 * Used in the PUT /tasks/{id}/assign endpoint to update task ownership and status.
 */
package com.disa.task_service.dto;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private Long assignedTo;
}