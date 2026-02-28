package com.example.personnel_service.dto;

import java.time.LocalDateTime;

public class PhysicalLimitationDto {
    private long id;
    private String limitation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
