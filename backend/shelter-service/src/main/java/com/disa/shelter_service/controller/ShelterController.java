package com.disa.shelter_service.controller;

import com.disa.shelter_service.dto.CheckInRequest;
import com.disa.shelter_service.dto.CheckOutRequest;
import com.disa.shelter_service.dto.ShelterRequest;
import com.disa.shelter_service.entity.Shelter;
import com.disa.shelter_service.entity.ShelterStatus;
import com.disa.shelter_service.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping
    public ResponseEntity<Shelter> createShelter(@Valid @RequestBody ShelterRequest request) {
        Shelter shelter = shelterService.createShelter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(shelter);
    }

    @GetMapping
    public ResponseEntity<List<Shelter>> getAllShelters() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelter> getShelterById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Shelter>> getAvailableShelters() {
        return ResponseEntity.ok(shelterService.getAvailableShelters());
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Shelter>> getNearbyShelters(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "50") Double radiusKm) {
        return ResponseEntity.ok(shelterService.getNearbyShelters(latitude, longitude, radiusKm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shelter> updateShelter(
            @PathVariable Long id,
            @Valid @RequestBody ShelterRequest request) {
        return ResponseEntity.ok(shelterService.updateShelter(id, request));
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<Shelter> checkIn(
            @PathVariable Long id,
            @Valid @RequestBody CheckInRequest request) {
        Shelter shelter = shelterService.checkIn(id, request.getNumberOfPeople());
        return ResponseEntity.ok(shelter);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<Shelter> checkOut(
            @PathVariable Long id,
            @Valid @RequestBody CheckOutRequest request) {
        Shelter shelter = shelterService.checkOut(id, request.getNumberOfPeople());
        return ResponseEntity.ok(shelter);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shelter> updateStatus(
            @PathVariable Long id,
            @RequestParam ShelterStatus status) {
        return ResponseEntity.ok(shelterService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.noContent().build();
    }
}
