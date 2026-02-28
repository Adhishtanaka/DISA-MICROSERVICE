/**
 * ShelterStatus.java
 *
 * Enumeration representing the operational status of an emergency shelter
 * in the DISA platform.
 *
 * Values:
 *   - OPERATIONAL      : The shelter is open and actively accepting evacuees
 *   - FULL             : The shelter has reached its maximum capacity
 *   - CLOSED           : The shelter is not currently accepting evacuees
 *   - UNDER_PREPARATION: The shelter is being prepared and will soon be operational
 */
package com.disa.shelter_service.entity;

public enum ShelterStatus {
    OPERATIONAL,        // Open and accepting evacuees
    FULL,              // At capacity
    CLOSED,            // Not accepting evacuees
    UNDER_PREPARATION  // Being prepared for use
}
