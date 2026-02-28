package com.example.personnel_service.repository;

import com.example.personnel_service.entity.InjuryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InjuryHistoryRepository extends JpaRepository<InjuryHistory, Long> {
}
