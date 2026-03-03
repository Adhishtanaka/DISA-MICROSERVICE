/*
 * EventConsumer listens for RabbitMQ events from other microservices and triggers
 * appropriate task service operations. Currently handles assessment completion events
 * to auto-generate follow-up response tasks.
 */
package com.disa.task_service.event;

import com.disa.task_service.config.RabbitMQConfig;
import com.disa.task_service.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final TaskService taskService;

    /**
     * Consumes {@code assessment.completed} messages from RabbitMQ and triggers
     * automatic task generation based on the required actions in the payload.
     *
     * @param event the deserialized assessment completion event
     */
    @RabbitListener(queues = RabbitMQConfig.ASSESSMENT_COMPLETED_QUEUE)
    public void handleAssessmentCompleted(AssessmentEvent event) {
        log.info("Received assessment.completed event: {}", event);

        // Auto-create follow-up tasks based on required actions
        taskService.createTasksFromAssessment(event.getPayload());
    }

    @RabbitListener(queues = RabbitMQConfig.PERSONNEL_STATUS_QUEUE)
    public void handlePersonnelStatusChanged(PersonnelStatusEvent event) {
        try {
            log.info("Received personnel.status.changed event: personnelId={}, status={}, available={}",
                    event.getPayload().getPersonnelId(),
                    event.getPayload().getStatus(),
                    event.getPayload().getIsAvailable());

            if (Boolean.FALSE.equals(event.getPayload().getIsAvailable())) {
                log.warn("Personnel {} ({}) is now UNAVAILABLE. Check IN_PROGRESS tasks assigned to this person.",
                        event.getPayload().getPersonnelCode(),
                        event.getPayload().getFullName());
            } else {
                log.info("Personnel {} ({}) is now AVAILABLE for task assignment.",
                        event.getPayload().getPersonnelCode(),
                        event.getPayload().getFullName());
            }
        } catch (Exception e) {
            log.error("Failed to process personnel.status.changed event: {}", e.getMessage(), e);
        }
    }
}