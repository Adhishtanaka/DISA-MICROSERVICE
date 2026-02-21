/**
 * CheckInRequest.java
 *
 * Data Transfer Object (DTO) representing a check-in request for a shelter.
 * Used to capture the number of people being checked into a specific shelter
 * via the POST /api/shelters/{id}/checkin endpoint.
 *
 * Validation:
 *   - numberOfPeople: must not be null and must be a positive integer
 */
package com.disa.shelter_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CheckInRequest {

    @NotNull(message = "Number of people is required")
    @Positive(message = "Number of people must be positive")
    private Integer numberOfPeople;

    public CheckInRequest() {}

    public CheckInRequest(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }
}
