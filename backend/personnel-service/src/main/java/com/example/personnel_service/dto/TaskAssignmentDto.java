package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentDto {
    private PersonDto assignedPerson;
    private TaskDto task;
    private String assignmentReason;
    private double matchScore;
}
