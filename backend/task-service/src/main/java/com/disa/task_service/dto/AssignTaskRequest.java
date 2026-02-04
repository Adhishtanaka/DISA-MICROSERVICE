/*
 * AssignTaskRequest DTO for task service
 */
package com.disa.task_service.dto;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private Long assignedTo;
}