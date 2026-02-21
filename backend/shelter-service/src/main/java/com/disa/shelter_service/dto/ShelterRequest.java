/**
 * ShelterRequest.java
 *
 * Data Transfer Object (DTO) for creating or updating a shelter resource.
 * Carries all the necessary input data from the client to the Shelter Service
 * for POST (create) and PUT (update) operations.
 *
 * Fields:
 *   - name            : Shelter name (required)
 *   - address         : Physical address of the shelter (required)
 *   - latitude        : Geographic latitude coordinate (required)
 *   - longitude       : Geographic longitude coordinate (required)
 *   - totalCapacity   : Maximum number of people the shelter can accommodate (required, positive)
 *   - currentOccupancy: Current number of occupants (defaults to 0)
 *   - status          : Operational status of the shelter (defaults to OPERATIONAL)
 *   - contactPerson   : Name of the shelter contact person
 *   - contactNumber   : Phone number of the shelter contact
 *   - facilities      : Description of available facilities (e.g., medical, food, water)
 */
package com.disa.shelter_service.dto;

import com.disa.shelter_service.entity.ShelterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ShelterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Total capacity is required")
    @Positive(message = "Total capacity must be positive")
    private Integer totalCapacity;

    private Integer currentOccupancy = 0;

    private ShelterStatus status = ShelterStatus.OPERATIONAL;

    private String contactPerson;
    private String contactNumber;
    private String facilities;

    public ShelterRequest() {}

    public ShelterRequest(String name, String address, Double latitude, Double longitude,
                          Integer totalCapacity, Integer currentOccupancy, ShelterStatus status,
                          String contactPerson, String contactNumber, String facilities) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalCapacity = totalCapacity;
        this.currentOccupancy = currentOccupancy;
        this.status = status;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.facilities = facilities;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(Integer totalCapacity) { this.totalCapacity = totalCapacity; }

    public Integer getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(Integer currentOccupancy) { this.currentOccupancy = currentOccupancy; }

    public ShelterStatus getStatus() { return status; }
    public void setStatus(ShelterStatus status) { this.status = status; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }
}
