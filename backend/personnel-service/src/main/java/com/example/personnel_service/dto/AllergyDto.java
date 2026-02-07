package com.example.personnel_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AllergyDto {
    private long id;

    private String type;
    private String allergyTo;

    private List<MedicationDto>
            medications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
