/*
 * TaskServiceImpl is the primary implementation of TaskService, handling all
 * business logic for disaster response task management. Responsibilities include
 * creating, updating, assigning, and completing tasks, as well as publishing
 * RabbitMQ events on assignment and auto-generating tasks from assessment events.
 */
package com.disa.task_service.service;

import com.disa.task_service.dto.AssignTaskRequest;
import com.disa.task_service.dto.TaskRequest;
import com.disa.task_service.dto.TaskResponse;
import com.disa.task_service.entity.Task;
import com.disa.task_service.entity.enums.Priority;
import com.disa.task_service.entity.enums.TaskStatus;
import com.disa.task_service.entity.enums.TaskType;
import com.disa.task_service.event.AssessmentEvent;
import com.disa.task_service.event.EventPublisher;
import com.disa.task_service.event.TaskEvent;
import com.disa.task_service.exception.TaskAlreadyCompletedException;
import com.disa.task_service.exception.TaskNotFoundException;
import com.disa.task_service.repository.TaskRepository;
import com.disa.task_service.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EventPublisher eventPublisher;

    /**
     * Creates a new task from the provided request, assigns a unique task code,
     * and persists it with an initial status of PENDING.
     *
     * @param request the task creation data
     * @return the saved task as a {@link TaskResponse}
     */
    @Override
    public TaskResponse createTask(TaskRequest request) {
        log.info("Creating new task of type {} for incident {}", request.getType(), request.getIncidentId());
        Task task = new Task();
        task.setType(request.getType());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setIncidentId(request.getIncidentId());
        task.setLocation(request.getLocation());
        task.setStatus(TaskStatus.PENDING);
        task.setTaskCode(generateTaskCode());

        Task saved = taskRepository.save(task);
        log.debug("Task created with code {}", saved.getTaskCode());
        return mapToResponse(saved);
    }

    /**
     * Returns all tasks in the system mapped to their response representation.
     *
     * @return list of all tasks as {@link TaskResponse} objects
     */
    @Override
    public List<TaskResponse> getAllTasks() {
        log.debug("Fetching all tasks");
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Looks up a task by its database ID.
     *
     * @param id the task ID
     * @return the matching task as a {@link TaskResponse}
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    @Override
    public TaskResponse getTaskById(Long id) {
        log.debug("Fetching task with id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return mapToResponse(task);
    }

    /**
     * Updates the mutable fields (type, title, description, priority, incidentId, location)
     * of an existing task identified by the given ID.
     *
     * @param id      the ID of the task to update
     * @param request the new field values
     * @return the updated task as a {@link TaskResponse}
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        log.info("Updating task with id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setType(request.getType());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setIncidentId(request.getIncidentId());
        task.setLocation(request.getLocation());

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    /**
     * Assigns a task to a personnel member, transitions its status to IN_PROGRESS,
     * and publishes a {@code task.assigned} event to the RabbitMQ exchange.
     *
     * @param id      the ID of the task to assign
     * @param request contains the personnel ID to assign the task to
     * @return the updated task as a {@link TaskResponse}
     * @throws TaskNotFoundException         if no task exists with the given ID
     * @throws TaskAlreadyCompletedException if the task status is already COMPLETED
     */
    @Override
    public TaskResponse assignTask(Long id, AssignTaskRequest request) {
        log.info("Assigning task {} to personnel {}", id, request.getAssignedTo());
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException(id);
        }

        task.setAssignedTo(request.getAssignedTo());
        task.setStatus(TaskStatus.IN_PROGRESS);

        Task saved = taskRepository.save(task);

        TaskEvent.TaskPayload payload = new TaskEvent.TaskPayload();
        payload.setTaskId(saved.getTaskCode());
        payload.setAssignedTo(saved.getAssignedTo().toString());
        payload.setTaskType(saved.getType().toString());
        payload.setPriority(saved.getPriority().toString());
        payload.setLocation(saved.getLocation());
        eventPublisher.publishTaskAssigned(payload);
        log.debug("Published task.assigned event for task {}", saved.getTaskCode());

        return mapToResponse(saved);
    }

    /**
     * Marks a task as COMPLETED and records the time of completion.
     *
     * @param id the ID of the task to complete
     * @return the updated task as a {@link TaskResponse}
     * @throws TaskNotFoundException         if no task exists with the given ID
     * @throws TaskAlreadyCompletedException if the task is already in COMPLETED status
     */
    @Override
    public TaskResponse completeTask(Long id) {
        log.info("Completing task with id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException(id);
        }

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    /**
     * Permanently deletes a task from the database.
     *
     * @param id the ID of the task to delete
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with id {}", id);
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Iterates over the required actions in the assessment payload and creates one task
     * per action. Task type is inferred from keywords in the action string.
     *
     * @param payload the assessment completion data including incident ID, location, and required actions
     */
    @Override
    public void createTasksFromAssessment(AssessmentEvent.AssessmentPayload payload) {
        log.info("Auto-generating {} tasks from assessment for incident {}",
                payload.getRequiredActions().size(), payload.getIncidentId());
        for (String action : payload.getRequiredActions()) {
            TaskRequest request = new TaskRequest();
            if (action.contains("rescue")) {
                request.setType(TaskType.RESCUE_OPERATION);
            } else if (action.contains("medical")) {
                request.setType(TaskType.MEDICAL_AID);
            } else {
                request.setType(TaskType.DEBRIS_REMOVAL);
            }
            request.setTitle(action);
            request.setDescription("Auto-generated from assessment");
            request.setPriority(Priority.HIGH);
            request.setIncidentId(payload.getIncidentId());
            request.setLocation(payload.getLocation());
            createTask(request);
        }
    }

    /**
     * Generates a unique task code using a TSK- prefix and the current epoch millisecond.
     *
     * @return a unique task code string
     */
    private String generateTaskCode() {
        return "TSK-" + System.currentTimeMillis();
    }

    /**
     * Maps a {@link Task} entity to a {@link TaskResponse} DTO.
     *
     * @param task the entity to map
     * @return the corresponding response DTO
     */
    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTaskCode(task.getTaskCode());
        response.setType(task.getType());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setIncidentId(task.getIncidentId());
        response.setAssignedTo(task.getAssignedTo());
        response.setLocation(task.getLocation());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setCompletedAt(task.getCompletedAt());
        return response;
    }
}