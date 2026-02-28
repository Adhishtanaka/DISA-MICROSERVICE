/**
 * ShelterService.java
 *
 * Service interface defining the contract for all shelter management operations
 * in the DISA Shelter Service microservice.
 *
 * Implementations of this interface provide the business logic for:
 *   - createShelter        : Register a new emergency shelter
 *   - getAllShelters        : Retrieve a list of all shelters
 *   - getShelterById       : Retrieve a specific shelter by its ID
 *   - updateShelter        : Update the details of an existing shelter
 *   - deleteShelter        : Remove a shelter from the system
 *   - getAvailableShelters : Retrieve shelters that are currently accepting evacuees
 *   - getNearbyShelters    : Retrieve shelters within a specified radius of a location
 *   - checkIn              : Register people checking into a shelter
 *   - checkOut             : Register people checking out of a shelter
 *   - updateStatus         : Manually update the operational status of a shelter
 *   - prepareNearbyShelters: Automatically prepare shelters near a reported incident
 */
package com.disa.shelter_service.service;

import com.disa.shelter_service.dto.ShelterRequest;
import com.disa.shelter_service.entity.Shelter;
import com.disa.shelter_service.entity.ShelterStatus;

import java.util.List;

public interface ShelterService {

    Shelter createShelter(ShelterRequest request);

    List<Shelter> getAllShelters();

    Shelter getShelterById(Long id);

    Shelter updateShelter(Long id, ShelterRequest request);

    void deleteShelter(Long id);

    List<Shelter> getAvailableShelters();

    List<Shelter> getNearbyShelters(Double latitude, Double longitude, Double radiusKm);

    Shelter checkIn(Long shelterId, Integer numberOfPeople);

    Shelter checkOut(Long shelterId, Integer numberOfPeople);

    Shelter updateStatus(Long id, ShelterStatus status);

    void prepareNearbyShelters(Double incidentLat, Double incidentLon, String severity);
}
