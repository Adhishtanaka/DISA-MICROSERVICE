/*
 * TaskResponse DTO returned to API consumers after task operations.
 * Contains the full task details including current status and timestamps.
 */
package com.disa.task_service.dto;

import com.disa.task_service.entity.enums.Priority;
import com.disa.task_service.entity.enums.TaskStatus;
import com.disa.task_service.entity.enums.TaskType;
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