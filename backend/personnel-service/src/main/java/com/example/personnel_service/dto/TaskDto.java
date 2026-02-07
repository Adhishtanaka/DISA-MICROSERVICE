package com.example.personnel_service.dto;

import java.time.LocalDateTime;

public class TaskDto {
    private long id;
    private String taskCode; // TSK-401

    private TaskTypeDto type; // RESCUE_OPERATION, MEDICAL_AID, DEBRIS_REMOVAL

    private String title;
    private String description;

    private PriorityDto priority; // LOW, MEDIUM, HIGH, URGENT

    private Long incidentId;
    private Long assignedTo; // Personnel ID

    private String location;

    private TaskStatusDto status; // PENDING, IN_PROGRESS, COMPLETED

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
