package com.example.personnel_service.service;

import com.example.personnel_service.entity.PhysicalLimitation;
import com.example.personnel_service.repository.PhysicalLimitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalLimitationService {
    private final PhysicalLimitationRepository repository;

    public PhysicalLimitationService(PhysicalLimitationRepository repository) {
        this.repository = repository;
    }

    public List<PhysicalLimitation> getAllPhysicalLimitations() {
        return repository.findAll();
    }

    public PhysicalLimitation getPhysicalLimitationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
    }

    public List<PhysicalLimitation> addPhysicalLimitation(List<PhysicalLimitation> physicalLimitations) {
        return repository.saveAll(physicalLimitations);
    }

    public List<PhysicalLimitation> updatePhysicalLimitation(List<PhysicalLimitation> physicalLimitations) {
        return repository.saveAll(physicalLimitations);
    }

    public PhysicalLimitation softDeletePhysicalLimitation(Long id) {
        PhysicalLimitation physicalLimitation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
        physicalLimitation.setDisabled(true);
        return repository.save(physicalLimitation);
    }

    public void deletePhysicalLimitation(Long id) {
        PhysicalLimitation physicalLimitation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Physical limitation not found"));
        repository.delete(physicalLimitation);
    }
}
