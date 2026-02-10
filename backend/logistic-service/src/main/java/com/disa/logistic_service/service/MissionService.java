package com.disa.logistic_service.service;
import com.disa.logistic_service.dto.MissionRequest;
import com.disa.logistic_service.entity.Mission;
import com.disa.logistic_service.entity.MissionStatus;
import com.disa.logistic_service.entity.MissionType;
import java.util.List;
/**
 * Service interface for managing missions in the logistics service.
 * This interface defines the contract for mission-related operations,
 * including creation, retrieval, updating, and deletion of missions.
 * * @author Dilina Mewan
 * @version 1.0
 */
public interface MissionService {
    Mission createMissionForIncident(Long incidentId, String destination, String severity);
    Mission createUrgentRescueMission(Long incidentId, String severity);
    Mission createResourceDeliveryMission(Long resourceId, String resourceType, String location);

    Mission createMission(MissionRequest request);
    List<Mission> getAllMissions();
    Mission getMissionById(Long id);
    List<Mission> getMissionsByStatus(MissionStatus status);
    List<Mission> getMissionsByType(MissionType type);
    Mission updateMissionStatus(Long id, MissionStatus status);
    void deleteMission(Long id);
}