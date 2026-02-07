package com.example.personnel_service.dto;

import java.time.LocalDateTime;

public class MedicationDto {
    private long id;
    private String name;
    private String dosage;
    private String frequency;
    private boolean isTreated;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
