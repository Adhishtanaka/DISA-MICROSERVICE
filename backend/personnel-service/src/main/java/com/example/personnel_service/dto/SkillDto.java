package com.example.personnel_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SkillDto {
    private long id;
    private int experienceYears;
    private int missionCount;
    private String profession;
    private String level;
    private List<DocumentDto> certifications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
