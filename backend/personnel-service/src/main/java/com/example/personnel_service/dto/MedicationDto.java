package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
