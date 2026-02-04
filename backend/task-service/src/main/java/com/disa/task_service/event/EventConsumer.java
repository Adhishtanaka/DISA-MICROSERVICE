/*
 * EventConsumer for task service
 */
package com.disa.task_service.event;

import com.disa.task_service.config.RabbitMQConfig;
import com.disa.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final TaskService taskService;

    @RabbitListener(queues = RabbitMQConfig.ASSESSMENT_COMPLETED_QUEUE)
    public void handleAssessmentCompleted(AssessmentEvent event) {
        log.info("Received assessment.completed event: {}", event);

        // Auto-create follow-up tasks based on required actions
        taskService.createTasksFromAssessment(event.getPayload());
    }
}