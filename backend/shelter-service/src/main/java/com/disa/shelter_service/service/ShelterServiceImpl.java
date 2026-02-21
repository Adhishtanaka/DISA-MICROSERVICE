package com.disa.shelter_service.service;

import com.disa.shelter_service.dto.ShelterRequest;
import com.disa.shelter_service.entity.Shelter;
import com.disa.shelter_service.entity.ShelterStatus;
import com.disa.shelter_service.repository.ShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShelterServiceImpl implements ShelterService {

    private static final Logger log = LoggerFactory.getLogger(ShelterServiceImpl.class);

    private final ShelterRepository shelterRepository;

    private static final double NEARBY_RADIUS_KM = 50.0; // 50km radius

    public ShelterServiceImpl(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Override
    @Transactional
    public Shelter createShelter(ShelterRequest request) {
        Shelter shelter = new Shelter();
        shelter.setShelterCode(generateShelterCode());
        shelter.setName(request.getName());
        shelter.setAddress(request.getAddress());
        shelter.setLatitude(request.getLatitude());
        shelter.setLongitude(request.getLongitude());
        shelter.setTotalCapacity(request.getTotalCapacity());
        shelter.setCurrentOccupancy(request.getCurrentOccupancy() != null ? request.getCurrentOccupancy() : 0);
        shelter.setStatus(request.getStatus() != null ? request.getStatus() : ShelterStatus.OPERATIONAL);
        shelter.setContactPerson(request.getContactPerson());
        shelter.setContactNumber(request.getContactNumber());
        shelter.setFacilities(request.getFacilities());

        Shelter saved = shelterRepository.save(shelter);
        log.info("Created shelter: {} with code: {}", saved.getName(), saved.getShelterCode());
        return saved;
    }

    @Override
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    @Override
    public Shelter getShelterById(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shelter not found with id: " + id));
    }

    @Override
    @Transactional
    public Shelter updateShelter(Long id, ShelterRequest request) {
        Shelter shelter = getShelterById(id);
        shelter.setName(request.getName());
        shelter.setAddress(request.getAddress());
        shelter.setLatitude(request.getLatitude());
        shelter.setLongitude(request.getLongitude());
        shelter.setTotalCapacity(request.getTotalCapacity());
        if (request.getCurrentOccupancy() != null) {
            shelter.setCurrentOccupancy(request.getCurrentOccupancy());
        }
        if (request.getStatus() != null) {
            shelter.setStatus(request.getStatus());
        }
        shelter.setContactPerson(request.getContactPerson());
        shelter.setContactNumber(request.getContactNumber());
        shelter.setFacilities(request.getFacilities());

        return shelterRepository.save(shelter);
    }

    @Override
    @Transactional
    public void deleteShelter(Long id) {
        Shelter shelter = getShelterById(id);
        shelterRepository.delete(shelter);
        log.info("Deleted shelter with id: {}", id);
    }

    @Override
    public List<Shelter> getAvailableShelters() {
        return shelterRepository.findByStatusIn(
                List.of(ShelterStatus.OPERATIONAL, ShelterStatus.UNDER_PREPARATION)
        );
    }

    @Override
    public List<Shelter> getNearbyShelters(Double latitude, Double longitude, Double radiusKm) {
        return findNearbyShelters(latitude, longitude, radiusKm);
    }

    @Override
    @Transactional
    public Shelter checkIn(Long shelterId, Integer numberOfPeople) {
        Shelter shelter = getShelterById(shelterId);

        if (shelter.getAvailableCapacity() < numberOfPeople) {
            throw new RuntimeException("Insufficient capacity. Available: " + shelter.getAvailableCapacity());
        }

        shelter.setCurrentOccupancy(shelter.getCurrentOccupancy() + numberOfPeople);

        // Update status if full
        if (shelter.isFull()) {
            shelter.setStatus(ShelterStatus.FULL);
        }

        log.info("Checked in {} people to shelter: {}", numberOfPeople, shelter.getShelterCode());
        return shelterRepository.save(shelter);
    }

    @Override
    @Transactional
    public Shelter checkOut(Long shelterId, Integer numberOfPeople) {
        Shelter shelter = getShelterById(shelterId);

        if (shelter.getCurrentOccupancy() < numberOfPeople) {
            throw new RuntimeException("Invalid checkout count. Current occupancy: " + shelter.getCurrentOccupancy());
        }

        shelter.setCurrentOccupancy(shelter.getCurrentOccupancy() - numberOfPeople);

        // Update status if no longer full
        if (shelter.getStatus() == ShelterStatus.FULL && !shelter.isFull()) {
            shelter.setStatus(ShelterStatus.OPERATIONAL);
        }

        log.info("Checked out {} people from shelter: {}", numberOfPeople, shelter.getShelterCode());
        return shelterRepository.save(shelter);
    }

    @Override
    @Transactional
    public Shelter updateStatus(Long id, ShelterStatus status) {
        Shelter shelter = getShelterById(id);
        shelter.setStatus(status);
        log.info("Updated shelter {} status to: {}", shelter.getShelterCode(), status);
        return shelterRepository.save(shelter);
    }

    @Override
    @Transactional
    public void prepareNearbyShelters(Double incidentLat, Double incidentLon, String severity) {
        // Find shelters within radius
        List<Shelter> nearbyShelters = findNearbyShelters(incidentLat, incidentLon, NEARBY_RADIUS_KM);

        log.info("Found {} nearby shelters for incident at ({}, {})",
                nearbyShelters.size(), incidentLat, incidentLon);

        for (Shelter shelter : nearbyShelters) {
            if (shelter.getStatus() == ShelterStatus.CLOSED) {
                shelter.setStatus(ShelterStatus.UNDER_PREPARATION);
                shelterRepository.save(shelter);
                log.info("Preparing shelter: {} for incident", shelter.getShelterCode());
            } else if (shelter.getStatus() == ShelterStatus.UNDER_PREPARATION) {
                shelter.setStatus(ShelterStatus.OPERATIONAL);
                shelterRepository.save(shelter);
                log.info("Opened shelter: {} for incident", shelter.getShelterCode());
            }
        }
    }

    private List<Shelter> findNearbyShelters(Double lat, Double lon, double radiusKm) {
        // Simple distance calculation (Haversine formula would be better)
        List<Shelter> allShelters = shelterRepository.findAll();

        return allShelters.stream()
                .filter(shelter -> {
                    if (shelter.getLatitude() == null || shelter.getLongitude() == null) {
                        return false;
                    }
                    double distance = calculateDistance(
                            lat, lon,
                            shelter.getLatitude(), shelter.getLongitude()
                    );
                    return distance <= radiusKm;
                })
                .toList();
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Simplified distance calculation (in km)
        // In production, use proper Haversine formula
        double latDiff = Math.abs(lat1 - lat2);
        double lonDiff = Math.abs(lon1 - lon2);
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111; // Rough conversion to km
    }

    private String generateShelterCode() {
        long count = shelterRepository.count() + 1;
        return String.format("SHE-%03d", count);
    }
}
