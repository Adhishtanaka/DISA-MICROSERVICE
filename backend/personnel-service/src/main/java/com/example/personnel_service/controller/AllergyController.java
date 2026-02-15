package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.service.AllergyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/allergies")
public class AllergyController {
    private final AllergyService allergyService;

    public AllergyController(AllergyService allergyService) {
        this.allergyService = allergyService;
    }

    @GetMapping
    public ResponseEntity<List<Allergy>> getAllAllergies() {
        return ResponseEntity.ok(allergyService.getAllAllergies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Allergy> getAllergyById(@PathVariable Long id) {
        return ResponseEntity.ok(allergyService.getAllergyById(id));
    }

    @PostMapping
    public ResponseEntity<List<Allergy>> createAllergies(@RequestBody List<Allergy> allergies) {
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyService.addAllergy(allergies));
    }

    @PutMapping
    public ResponseEntity<List<Allergy>> updateAllergies(@RequestBody List<Allergy> allergies) {
        return ResponseEntity.ok(allergyService.updateAllergy(allergies));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Allergy> softDeleteAllergy(@PathVariable Long id) {
        return ResponseEntity.ok(allergyService.softDeleteAllergy(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long id) {
        allergyService.deleteAllergy(id);
        return ResponseEntity.noContent().build();
    }
}
