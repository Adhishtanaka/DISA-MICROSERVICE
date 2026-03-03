package com.example.personnel_service.repository;

import com.example.personnel_service.entity.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentHistoryRepository extends JpaRepository<AssignmentHistory, Long> {

    List<AssignmentHistory> findByPersonIdOrderByAssignedAtDesc(Long personId);

    List<AssignmentHistory> findByPersonIdAndStatus(Long personId, String status);

    Optional<AssignmentHistory> findByTaskIdAndStatus(Long taskId, String status);
}
