package com.example.personnel_service.controller;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.service.EmergencyContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/emergency-contacts")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContact>> getAllEmergencyContacts() {
        return ResponseEntity.ok(emergencyContactService.getAllEmergencyContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyContactService.getEmergencyContactById(id));
    }

    @PostMapping
    public ResponseEntity<List<EmergencyContact>> createEmergencyContacts(@RequestBody List<EmergencyContact> emergencyContacts) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyContactService.addEmergencyContact(emergencyContacts));
    }

    @PutMapping
    public ResponseEntity<List<EmergencyContact>> updateEmergencyContacts(@RequestBody List<EmergencyContact> emergencyContacts) {
        return ResponseEntity.ok(emergencyContactService.updateEmergencyContact(emergencyContacts));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmergencyContact> softDeleteEmergencyContact(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyContactService.softDeleteEmergencyContact(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id) {
        emergencyContactService.deleteEmergencyContact(id);
        return ResponseEntity.noContent().build();
    }
}
