package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
    private long id;
    private String skillName; // profession
    private int experienceYears;
    private int missionCount;
    private String proficiencyLevel; // level
    private List<DocumentDto> certifications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
