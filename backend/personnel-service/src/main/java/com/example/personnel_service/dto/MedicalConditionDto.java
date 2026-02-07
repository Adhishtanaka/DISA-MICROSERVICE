package com.example.personnel_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalConditionDto {
    private long id;
    private String bloodGroup;
    private String height;
    private String weight;
    private List<AllergyDto> allergies;
    private List<ChronicConditionDto> chronicConditions;
    private List<PhysicalLimitationDto> physicalLimitations;
    private List<DocumentDto> medicalDocuments;
    private List<InjuryHistoryDto> pastInjuries;
    private List<MedicationDto> medications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
