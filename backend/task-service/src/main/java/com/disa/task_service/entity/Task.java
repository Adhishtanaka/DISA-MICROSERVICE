/*
 * Task entity for task service
 */
package com.disa.task_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskCode; // TSK-401

    @Enumerated(EnumType.STRING)
    private TaskType type; // RESCUE_OPERATION, MEDICAL_AID, DEBRIS_REMOVAL

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority; // LOW, MEDIUM, HIGH, URGENT

    private Long incidentId;
    private Long assignedTo; // Personnel ID

    private String location;

    @Enumerated(EnumType.STRING)
    private TaskStatus status; // PENDING, IN_PROGRESS, COMPLETED

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}