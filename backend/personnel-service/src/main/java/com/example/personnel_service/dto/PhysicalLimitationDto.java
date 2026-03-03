package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalLimitationDto {
    private long id;
    private String limitation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
