/*
 * TaskRequest DTO for task service
 */
package com.disa.task_service.dto;

import com.disa.task_service.entity.TaskType;
import com.disa.task_service.entity.Priority;
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