package com.example.personnel_service.dto;

import com.example.personnel_service.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private long id;
    private String personalCode;
    private String firstName;
    private String lastName;

    private String phone;
    private String Email;
    private String address;
    private List<EmergencyContactDto> emergencyContacts;

    private String role;
    private String department;
    private String organization;
    private String rank;

    private MedicalConditionDto medicalCondition;

    private List<SkillDto> skills;

    private String status;// Inactive, On Leave, On Duty, Available
    private LocalDateTime shiftStartTime;
    private LocalDateTime shiftEndTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDisabled;

}
