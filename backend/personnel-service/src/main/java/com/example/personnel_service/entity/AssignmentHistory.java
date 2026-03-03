package com.example.personnel_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AssignmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    private Long taskId;
    private String taskCode;
    private String taskTitle;
    private String taskType;
    private String assignmentReason;
    private double matchScore;
    private String status; // ACTIVE, COMPLETED, CANCELLED

    @CreationTimestamp
    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;
}
