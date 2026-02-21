package com.disa.shelter_service.entity;

public enum ShelterStatus {
    OPERATIONAL,        // Open and accepting evacuees
    FULL,              // At capacity
    CLOSED,            // Not accepting evacuees
    UNDER_PREPARATION  // Being prepared for use
}
