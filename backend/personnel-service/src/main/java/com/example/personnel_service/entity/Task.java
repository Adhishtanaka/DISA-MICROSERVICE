package com.example.personnel_service.entity;

import java.time.LocalDateTime;
import java.util.Date;


public class Task {


    private Long id;

    private String taskCode; // TSK-401


    private TaskType type; // RESCUE_OPERATION, MEDICAL_AID, DEBRIS_REMOVAL

    private String title;
    private String description;


    private Priority priority; // LOW, MEDIUM, HIGH, URGENT

    private Long incidentId;
    private Long assignedTo; // Personnel ID

    private String location;


    private TaskStatus status; // PENDING, IN_PROGRESS, COMPLETED


    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}

