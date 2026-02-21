/**
 * ShelterResponse.java
 *
 * Data Transfer Object (DTO) representing the response payload returned to clients
 * when querying shelter information from the Shelter Service.
 *
 * Fields:
 *   - id                  : Unique database identifier of the shelter
 *   - shelterCode         : Human-readable shelter code (e.g., SHE-001)
 *   - name                : Name of the shelter
 *   - address             : Physical address of the shelter
 *   - latitude            : Geographic latitude coordinate
 *   - longitude           : Geographic longitude coordinate
 *   - totalCapacity       : Maximum number of people the shelter can accommodate
 *   - currentOccupancy    : Current number of occupants
 *   - availableCapacity   : Remaining capacity (totalCapacity - currentOccupancy)
 *   - occupancyPercentage : Percentage of capacity currently in use
 *   - status              : Operational status (OPERATIONAL, FULL, CLOSED, UNDER_PREPARATION)
 *   - contactPerson       : Name of the shelter contact person
 *   - contactNumber       : Phone number of the shelter contact
 *   - facilities          : Description of available facilities
 *   - createdAt           : Timestamp when the shelter record was created
 *   - updatedAt           : Timestamp when the shelter record was last updated
 */
package com.disa.shelter_service.dto;

import com.disa.shelter_service.entity.ShelterStatus;

import java.time.LocalDateTime;

public class ShelterResponse {

    private Long id;
    private String shelterCode;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer totalCapacity;
    private Integer currentOccupancy;
    private Integer availableCapacity;
    private Double occupancyPercentage;
    private ShelterStatus status;
    private String contactPerson;
    private String contactNumber;
    private String facilities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ShelterResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShelterCode() { return shelterCode; }
    public void setShelterCode(String shelterCode) { this.shelterCode = shelterCode; }

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

    public Integer getAvailableCapacity() { return availableCapacity; }
    public void setAvailableCapacity(Integer availableCapacity) { this.availableCapacity = availableCapacity; }

    public Double getOccupancyPercentage() { return occupancyPercentage; }
    public void setOccupancyPercentage(Double occupancyPercentage) { this.occupancyPercentage = occupancyPercentage; }

    public ShelterStatus getStatus() { return status; }
    public void setStatus(ShelterStatus status) { this.status = status; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
