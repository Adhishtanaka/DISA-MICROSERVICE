package com.example.personnel_service.service;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.repository.EmergencyContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyContactService {
    private final EmergencyContactRepository repository;

    public EmergencyContactService(EmergencyContactRepository repository) {
        this.repository = repository;
    }

    public List<EmergencyContact> getAllEmergencyContacts() {
        return repository.findAll();
    }

    public EmergencyContact getEmergencyContactById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
    }

    public List<EmergencyContact> addEmergencyContact(List<EmergencyContact> emergencyContacts) {
        return repository.saveAll(emergencyContacts);
    }

    public List<EmergencyContact> updateEmergencyContact(List<EmergencyContact> emergencyContacts) {
        return repository.saveAll(emergencyContacts);
    }

    public EmergencyContact softDeleteEmergencyContact(Long id) {
        EmergencyContact emergencyContact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
        emergencyContact.setDisabled(true);
        return repository.save(emergencyContact);
    }

    public void deleteEmergencyContact(Long id) {
        EmergencyContact emergencyContact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency contact not found"));
        repository.delete(emergencyContact);
    }
}
