package com.example.personnel_service.dto;

import java.time.LocalDateTime;

public class EmergencyContactDto {
    private long id;
    private String name;
    private String telephone;
    private String address;
    private String relation;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;
}
