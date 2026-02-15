package com.example.personnel_service.service;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.repository.AllergyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyService {
    private final AllergyRepository _allergyRepository;

    public AllergyService(AllergyRepository allergyRepository) {
        this._allergyRepository = allergyRepository;
    }

    public List<Allergy> getAllAllergies() {
        return _allergyRepository.findAll();
    }

    public Allergy getAllergyById(Long id) {
        return _allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found"));
    }

    public List<Allergy> addAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }

    public List<Allergy> updateAllergy(List<Allergy> allergy) {
        return _allergyRepository.saveAll(allergy);
    }
    public Allergy softDeleteAllergy(Long id) {
        Allergy allergy = _allergyRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Allergy not found"));
        allergy.setDisabled(true);
        return _allergyRepository.save(allergy);
    }
    public void deleteAllergy(long id) {
        Allergy allergy = _allergyRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Allergy not found"));
        _allergyRepository.delete(allergy);
    }
}
