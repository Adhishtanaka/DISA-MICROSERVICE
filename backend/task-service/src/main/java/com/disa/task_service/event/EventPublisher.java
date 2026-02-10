/*
 * EventPublisher for task service
 */
package com.disa.task_service.event;

import com.disa.task_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishTaskAssigned(TaskEvent.TaskPayload payload) {
        TaskEvent event = new TaskEvent();
        event.setEventType("task.assigned");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(payload);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.TASK_ASSIGNED_KEY, event);
    }
}