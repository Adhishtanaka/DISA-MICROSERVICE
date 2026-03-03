package com.example.personnel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
