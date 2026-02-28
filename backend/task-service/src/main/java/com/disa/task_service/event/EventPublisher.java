/*
 * EventPublisher is responsible for broadcasting task-related domain events
 * to the RabbitMQ topic exchange. Used when significant state changes occur,
 * such as task assignment, enabling other microservices to react accordingly.
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

    /**
     * Wraps the given payload in a {@link TaskEvent} and sends it to the
     * {@code disaster.topic.exchange} with the {@code task.assigned} routing key.
     *
     * @param payload the task assignment data to include in the event
     */
    public void publishTaskAssigned(TaskEvent.TaskPayload payload) {
        TaskEvent event = new TaskEvent();
        event.setEventType("task.assigned");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(payload);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.TASK_ASSIGNED_KEY, event);
    }
}