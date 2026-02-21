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
