/**
 * CheckOutRequest.java
 *
 * Data Transfer Object (DTO) representing a check-out request for a shelter.
 * Used to capture the number of people being checked out from a specific shelter
 * via the POST /api/shelters/{id}/checkout endpoint.
 *
 * Validation:
 *   - numberOfPeople: must not be null and must be a positive integer
 */
package com.disa.shelter_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CheckOutRequest {

    @NotNull(message = "Number of people is required")
    @Positive(message = "Number of people must be positive")
    private Integer numberOfPeople;

    public CheckOutRequest() {}

    public CheckOutRequest(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }
}
