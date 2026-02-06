package com.example.personnel_service.dto;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.entity.Skill;

import java.util.Date;
import java.util.List;

public class PersonDto {
    private long id;
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

    private List<Skill> skills;

    private String status;
    private Date shiftStartTime;
    private Date shiftEndTime;


}
