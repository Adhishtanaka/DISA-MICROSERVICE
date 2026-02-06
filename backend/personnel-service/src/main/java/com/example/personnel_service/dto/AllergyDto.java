package com.example.personnel_service.dto;

import java.util.List;

public class AllergyDto {
    private long id;
    private String type;
    private String allergyTo;
    private List<MedicationDto> medications;
}
