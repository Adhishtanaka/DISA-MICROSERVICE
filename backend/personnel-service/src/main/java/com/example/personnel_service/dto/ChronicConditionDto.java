package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChronicConditionDto {
    private long id;
    private String name;
    private String severity;

    private List<MedicationDto>
            medications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
