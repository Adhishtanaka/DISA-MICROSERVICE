package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentHistoryDto {
    private Long id;
    private Long personId;
    private String personName;
    private Long taskId;
    private String taskCode;
    private String taskTitle;
    private String taskType;
    private String assignmentReason;
    private double matchScore;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}
