/*
 * TaskService interface defining the contract for all task management operations
 * within the disaster response task service. Implementations handle CRUD operations,
 * task assignment, completion, and event-driven task creation from assessment results.
 */
package com.disa.task_service.service.interfaces;

import com.disa.task_service.dto.AssignTaskRequest;
import com.disa.task_service.dto.TaskRequest;
import com.disa.task_service.dto.TaskResponse;
import com.disa.task_service.event.AssessmentEvent;

import java.util.List;

public interface TaskService {

    /**
     * Creates a new task and persists it with PENDING status.
     *
     * @param request the task data including type, title, priority, and location
     * @return the persisted task as a {@link TaskResponse}
     */
    TaskResponse createTask(TaskRequest request);

    /**
     * Retrieves all tasks currently stored in the system.
     *
     * @return list of all tasks as {@link TaskResponse} objects
     */
    List<TaskResponse> getAllTasks();

    /**
     * Retrieves a single task by its database ID.
     *
     * @param id the task ID
     * @return the matching task as a {@link TaskResponse}
     * @throws com.disa.task_service.exception.TaskNotFoundException if no task exists with the given ID
     */
    TaskResponse getTaskById(Long id);

    /**
     * Updates the mutable fields of an existing task.
     *
     * @param id      the ID of the task to update
     * @param request the new task data
     * @return the updated task as a {@link TaskResponse}
     * @throws com.disa.task_service.exception.TaskNotFoundException if no task exists with the given ID
     */
    TaskResponse updateTask(Long id, TaskRequest request);

    /**
     * Assigns a task to a personnel member and transitions its status to IN_PROGRESS.
     * Publishes a {@code task.assigned} event to RabbitMQ after a successful assignment.
     *
     * @param id      the ID of the task to assign
     * @param request the assignment data containing the personnel ID
     * @return the updated task as a {@link TaskResponse}
     * @throws com.disa.task_service.exception.TaskNotFoundException         if no task exists with the given ID
     * @throws com.disa.task_service.exception.TaskAlreadyCompletedException if the task is already completed
     */
    TaskResponse assignTask(Long id, AssignTaskRequest request);

    /**
     * Marks a task as COMPLETED and records the completion timestamp.
     *
     * @param id the ID of the task to complete
     * @return the updated task as a {@link TaskResponse}
     * @throws com.disa.task_service.exception.TaskNotFoundException         if no task exists with the given ID
     * @throws com.disa.task_service.exception.TaskAlreadyCompletedException if the task is already completed
     */
    TaskResponse completeTask(Long id);

    /**
     * Permanently removes a task from the system.
     *
     * @param id the ID of the task to delete
     * @throws com.disa.task_service.exception.TaskNotFoundException if no task exists with the given ID
     */
    void deleteTask(Long id);

    /**
     * Auto-generates one task per required action from an assessment payload.
     * Task types are inferred from action keywords (rescue → RESCUE_OPERATION,
     * medical → MEDICAL_AID, otherwise → DEBRIS_REMOVAL).
     *
     * @param payload the assessment data including incident ID, location, and required actions
     */
    void createTasksFromAssessment(AssessmentEvent.AssessmentPayload payload);
}
