package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
public class TaskDto {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public TaskTypeDto getType() {
        return type;
    }

    public void setType(TaskTypeDto type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityDto getPriority() {
        return priority;
    }

    public void setPriority(PriorityDto priority) {
        this.priority = priority;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TaskStatusDto getStatus() {
        return status;
    }

    public void setStatus(TaskStatusDto status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

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
