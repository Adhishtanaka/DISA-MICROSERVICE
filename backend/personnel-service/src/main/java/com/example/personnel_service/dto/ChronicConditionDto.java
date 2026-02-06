package com.example.personnel_service.dto;

import java.util.List;

public class ChronicConditionDto {
    private long id;
    private String name;
    private String severity;
    private List<MedicationDto> medications;
}
