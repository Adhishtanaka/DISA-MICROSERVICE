/*
 * TaskResponse DTO for task service
 */
package com.disa.task_service.dto;

import com.disa.task_service.entity.TaskType;
import com.disa.task_service.entity.Priority;
import com.disa.task_service.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String taskCode;
    private TaskType type;
    private String title;
    private String description;
    private Priority priority;
    private Long incidentId;
    private Long assignedTo;
    private String location;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}