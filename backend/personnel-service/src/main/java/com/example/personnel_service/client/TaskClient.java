package com.example.personnel_service.client;

import com.example.personnel_service.dto.TaskDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import com.example.personnel_service.dto.*;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Client class for fetching task data.
 * Currently provides mock task data for disaster management operations.
 * In production, this would integrate with the Task Service microservice.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Component
public class TaskClient {

    /**
     * Fetches a list of tasks from the task service.
     * Currently returns mock data for testing purposes.
     * 
     * @return list of TaskDto objects representing disaster management tasks
     */
    public List<TaskDto> fetchTasks() {

        List<TaskDto> tasks = new ArrayList<>();

        tasks.add(task(1,"TSK-401", TaskTypeDto.RESCUE_OPERATION, PriorityDto.URGENT, "Colombo", TaskStatusDto.PENDING));
        tasks.add(task(2,"TSK-402", TaskTypeDto.MEDICAL_AID, PriorityDto.HIGH, "Galle", TaskStatusDto.IN_PROGRESS));
        tasks.add(task(3,"TSK-403", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.MEDIUM, "Kandy", TaskStatusDto.PENDING));
        tasks.add(task(4,"TSK-404", TaskTypeDto.RESCUE_OPERATION, PriorityDto.URGENT, "Jaffna", TaskStatusDto.PENDING));
        tasks.add(task(5,"TSK-405", TaskTypeDto.MEDICAL_AID, PriorityDto.HIGH, "Kurunegala", TaskStatusDto.IN_PROGRESS));
        tasks.add(task(6,"TSK-406", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.LOW, "Matara", TaskStatusDto.PENDING));
        tasks.add(task(7,"TSK-407", TaskTypeDto.RESCUE_OPERATION, PriorityDto.MEDIUM, "Ratnapura", TaskStatusDto.PENDING));
        tasks.add(task(8,"TSK-408", TaskTypeDto.MEDICAL_AID, PriorityDto.LOW, "Anuradhapura", TaskStatusDto.COMPLETED));
        tasks.add(task(9,"TSK-409", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.HIGH, "Negombo", TaskStatusDto.IN_PROGRESS));
        tasks.add(task(10,"TSK-410", TaskTypeDto.RESCUE_OPERATION, PriorityDto.URGENT, "Badulla", TaskStatusDto.PENDING));

        // more 10
        tasks.add(task(11,"TSK-411", TaskTypeDto.MEDICAL_AID, PriorityDto.MEDIUM, "Kalutara", TaskStatusDto.PENDING));
        tasks.add(task(12,"TSK-412", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.LOW, "Hambantota", TaskStatusDto.PENDING));
        tasks.add(task(13,"TSK-413", TaskTypeDto.RESCUE_OPERATION, PriorityDto.HIGH, "Nuwara Eliya", TaskStatusDto.IN_PROGRESS));
        tasks.add(task(14,"TSK-414", TaskTypeDto.MEDICAL_AID, PriorityDto.URGENT, "Trincomalee", TaskStatusDto.PENDING));
        tasks.add(task(15,"TSK-415", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.MEDIUM, "Batticaloa", TaskStatusDto.PENDING));
        tasks.add(task(16,"TSK-416", TaskTypeDto.RESCUE_OPERATION, PriorityDto.LOW, "Chilaw", TaskStatusDto.COMPLETED));
        tasks.add(task(17,"TSK-417", TaskTypeDto.MEDICAL_AID, PriorityDto.HIGH, "Avissawella", TaskStatusDto.IN_PROGRESS));
        tasks.add(task(18,"TSK-418", TaskTypeDto.DEBRIS_REMOVAL, PriorityDto.URGENT, "Monaragala", TaskStatusDto.PENDING));
        tasks.add(task(19,"TSK-419", TaskTypeDto.RESCUE_OPERATION, PriorityDto.MEDIUM, "Polonnaruwa", TaskStatusDto.PENDING));
        tasks.add(task(20,"TSK-420", TaskTypeDto.MEDICAL_AID, PriorityDto.LOW, "Puttalam", TaskStatusDto.PENDING));

        return tasks;
    }

    /**
     * Helper method to create a TaskDto with the specified parameters.
     * 
     * @param id the unique identifier for the task
     * @param code the task code (e.g., TSK-401)
     * @param type the type of task (RESCUE_OPERATION, MEDICAL_AID, DEBRIS_REMOVAL)
     * @param priority the priority level of the task
     * @param location the location where the task needs to be performed
     * @param status the current status of the task
     * @return a fully configured TaskDto object
     */
    private TaskDto task(long id, String code,
                         TaskTypeDto type, PriorityDto priority,
                         String location, TaskStatusDto status) {

        TaskDto t = new TaskDto();
        t.setId(id);
        t.setTaskCode(code);
        t.setType(type);
        t.setTitle(type + " Task");
        t.setDescription("Operation at " + location);
        t.setPriority(priority);
        t.setIncidentId(1000L + id);
        t.setAssignedTo(null);
        t.setLocation(location);
        t.setStatus(status);
        t.setCreatedAt(LocalDateTime.now().minusHours(id));
        t.setCompletedAt(status == TaskStatusDto.COMPLETED ? LocalDateTime.now() : null);

        return t;
    }
}
