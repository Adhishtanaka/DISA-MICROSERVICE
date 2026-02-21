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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request payload for checking people into a shelter")
public class CheckInRequest {

    @Schema(description = "Number of people to check in (must be a positive integer)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
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
