package com.disa.mission_service.service.impl;

import com.disa.mission_service.dto.MissionRequest;
import com.disa.mission_service.entity.*;
import com.disa.mission_service.entity.enums.MissionStatus;
import com.disa.mission_service.entity.enums.MissionType;
import com.disa.mission_service.repository.MissionRepository;
import com.disa.mission_service.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of MissionService.
 * Handles the creation, retrieval, and status updates of logistics missions.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    @Override
    @Transactional
    public Mission createMissionForIncident(Long incidentId, String destination, String severity) {
        log.info("Creating delivery mission for incident ID: {}", incidentId);
        Mission mission = new Mission();
        mission.setMissionCode(generateMissionCode());
        mission.setType(MissionType.DELIVERY);
        mission.setIncidentId(incidentId);
        mission.setOrigin("Central Warehouse");
        mission.setDestination(destination);
        mission.setStatus(MissionStatus.PENDING);
        mission.setDescription("Emergency supplies delivery. Severity: " + severity);
        mission.setCargoDetails("Food, Water, Medical Supplies");
        mission.setVehicleType("TRUCK");

        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Mission createUrgentRescueMission(Long incidentId, String severity) {
        log.info("Creating RESCUE mission for escalated incident ID: {}", incidentId);
        Mission mission = new Mission();
        mission.setMissionCode(generateMissionCode());
        mission.setType(MissionType.RESCUE);
        mission.setIncidentId(incidentId);
        mission.setOrigin("Fire Station HQ");
        mission.setStatus(MissionStatus.PENDING);
        mission.setDescription("Urgent rescue operation - Escalated Incident");
        mission.setVehicleType("AMBULANCE");

        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Mission createResourceDeliveryMission(Long resourceId, String resourceType, String location) {
        log.info("Creating restocking mission for resource ID: {}", resourceId);
        Mission mission = new Mission();
        mission.setMissionCode(generateMissionCode());
        mission.setType(MissionType.DELIVERY);
        mission.setResourceId(resourceId);
        mission.setOrigin("Central Warehouse");
        mission.setDestination(location);
        mission.setStatus(MissionStatus.PENDING);
        mission.setDescription("Critical resource delivery - Low Stock Alert");
        mission.setCargoDetails(resourceType + " supplies");
        mission.setVehicleType("TRUCK");

        return missionRepository.save(mission);
    }

    @Override
    public Mission createMission(MissionRequest request) {
        Mission mission = new Mission();
        mission.setMissionCode(generateMissionCode());
        mission.setType(request.getType());
        mission.setOrigin(request.getOrigin());
        mission.setDestination(request.getDestination());
        mission.setDescription(request.getDescription());
        mission.setCargoDetails(request.getCargoDetails());
        mission.setVehicleType(request.getVehicleType());
        mission.setStatus(MissionStatus.PENDING);
        return missionRepository.save(mission);
    }

    @Override
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Override
    public Mission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with ID: " + id));
    }

    @Override
    public List<Mission> getMissionsByStatus(MissionStatus status) {
        return missionRepository.findByStatus(status);
    }

    @Override
    public List<Mission> getMissionsByType(MissionType type) {
        return missionRepository.findByType(type);
    }

    @Override
    @Transactional
    public Mission updateMissionStatus(Long id, MissionStatus status) {
        Mission mission = getMissionById(id);
        mission.setStatus(status);

        if (status == MissionStatus.IN_PROGRESS && mission.getStartedAt() == null) {
            mission.setStartedAt(LocalDateTime.now());
        } else if (status == MissionStatus.COMPLETED) {
            mission.setCompletedAt(LocalDateTime.now());
        }

        return missionRepository.save(mission);
    }

    @Override
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    // Helper to generate readable codes like MIS-005
    private String generateMissionCode() {
        long count = missionRepository.count();
        return String.format("MIS-%03d", count + 1);
    }
}
