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
