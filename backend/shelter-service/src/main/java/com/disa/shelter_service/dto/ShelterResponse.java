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
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response payload representing an emergency shelter")
public class ShelterResponse {

    @Schema(description = "Unique database identifier of the shelter", example = "1")
    private Long id;
    @Schema(description = "Human-readable shelter code", example = "SHE-001")
    private String shelterCode;
    @Schema(description = "Name of the shelter", example = "City Community Hall")
    private String name;
    @Schema(description = "Physical address of the shelter", example = "123 Main Street, Colombo")
    private String address;
    @Schema(description = "Geographic latitude of the shelter", example = "6.9271")
    private Double latitude;
    @Schema(description = "Geographic longitude of the shelter", example = "79.8612")
    private Double longitude;
    @Schema(description = "Maximum number of people the shelter can accommodate", example = "500")
    private Integer totalCapacity;
    @Schema(description = "Current number of occupants", example = "120")
    private Integer currentOccupancy;
    @Schema(description = "Remaining available capacity", example = "380")
    private Integer availableCapacity;
    @Schema(description = "Percentage of capacity currently in use", example = "24.0")
    private Double occupancyPercentage;
    @Schema(description = "Operational status of the shelter", example = "OPERATIONAL")
    private ShelterStatus status;
    @Schema(description = "Name of the shelter contact person", example = "John Silva")
    private String contactPerson;
    @Schema(description = "Phone number of the shelter contact", example = "+94771234567")
    private String contactNumber;
    @Schema(description = "Available facilities at the shelter", example = "Medical aid, Food, Water, Sanitation")
    private String facilities;
    @Schema(description = "Timestamp when the shelter record was created")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the shelter record was last updated")
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
