package com.example.personnel_service.service;

import com.example.personnel_service.entity.Medication;
import com.example.personnel_service.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {
    private final MedicationRepository repository;

    public MedicationService(MedicationRepository repository) {
        this.repository = repository;
    }

    public List<Medication> getAllMedications() {
        return repository.findAll();
    }

    public Medication getMedicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
    }

    public List<Medication> addMedication(List<Medication> medications) {
        return repository.saveAll(medications);
    }

    public List<Medication> updateMedication(List<Medication> medications) {
        return repository.saveAll(medications);
    }

    public Medication softDeleteMedication(Long id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        medication.setDisabled(true);
        return repository.save(medication);
    }

    public void deleteMedication(Long id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        repository.delete(medication);
    }
}
