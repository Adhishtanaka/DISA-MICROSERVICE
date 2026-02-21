package com.disa.shelter_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shelterCode; // SHE-501

    private String name;
    private String address;

    private Double latitude;
    private Double longitude;

    private Integer totalCapacity;
    private Integer currentOccupancy;

    @Enumerated(EnumType.STRING)
    private ShelterStatus status; // OPERATIONAL, FULL, CLOSED, UNDER_PREPARATION

    private String contactPerson;
    private String contactNumber;

    @Column(length = 1000)
    private String facilities; // Available facilities (medical, food, water, etc.)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Shelter() {}

    public Shelter(Long id, String shelterCode, String name, String address,
                   Double latitude, Double longitude, Integer totalCapacity,
                   Integer currentOccupancy, ShelterStatus status,
                   String contactPerson, String contactNumber, String facilities,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.shelterCode = shelterCode;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
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

    // Helper methods
    public Integer getAvailableCapacity() {
        return totalCapacity - currentOccupancy;
    }

    public boolean isFull() {
        return currentOccupancy >= totalCapacity;
    }

    public Double getOccupancyPercentage() {
        return (currentOccupancy.doubleValue() / totalCapacity.doubleValue()) * 100;
    }

    @Override
    public String toString() {
        return "Shelter{id=" + id + ", shelterCode='" + shelterCode + "', name='" + name + "', status=" + status + "}";
    }
}
