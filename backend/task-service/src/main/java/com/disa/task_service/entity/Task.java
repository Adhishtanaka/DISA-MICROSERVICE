/*
 * Task entity representing a disaster response task assigned to field personnel.
 * Tracks the full lifecycle of a task from creation through assignment to completion,
 * including metadata such as type, priority, location, and incident association.
 */
package com.disa.task_service.entity;

import com.disa.task_service.entity.enums.Priority;
import com.disa.task_service.entity.enums.TaskStatus;
import com.disa.task_service.entity.enums.TaskType;
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

    private String taskCode;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Long incidentId;
    private Long assignedTo;

    private String location;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}