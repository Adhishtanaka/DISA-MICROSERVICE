package com.disa.mission_service.repository;
import com.disa.mission_service.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.disa.mission_service.entity.enums.MissionType;
import com.disa.mission_service.entity.enums.MissionStatus;
import java.util.List;
/** * Repository interface for managing Mission entities in the logistics service.
 * This interface extends JpaRepository to provide CRUD operations and custom query methods for Mission entities.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // Custom finder methods for specific business requirements
    List<Mission> findByType(MissionType type);
    List<Mission> findByStatus(MissionStatus status);
    List<Mission> findByDriverId(String driverId);
    List<Mission> findByVehicleId(String vehicleId);
    List<Mission> findByOrigin(String origin);
    List<Mission> findByDestination(String destination);
    List<Mission> findByIncidentId(Long incidentId);
    List<Mission> findByResourceId(Long resourceId);
    List<Mission> findByTypeAndStatus(MissionType type, MissionStatus status);
}