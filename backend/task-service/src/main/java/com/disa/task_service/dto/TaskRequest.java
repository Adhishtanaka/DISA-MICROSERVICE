/*
 * TaskRequest DTO carrying the input data required to create or update a task.
 * Includes task classification, description, priority level, and location details.
 */
package com.disa.task_service.dto;

import com.disa.task_service.entity.enums.Priority;
import com.disa.task_service.entity.enums.TaskType;
import lombok.Data;

@Data
public class TaskRequest {
    private TaskType type;
    private String title;
    private String description;
    private Priority priority;
    private Long incidentId;
    private String location;
}