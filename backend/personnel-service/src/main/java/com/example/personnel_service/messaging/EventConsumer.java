package com.example.personnel_service.messaging;

import com.example.personnel_service.config.RabbitMQConfig;
import com.example.personnel_service.entity.Person;
import com.example.personnel_service.event.TaskAssignedEvent;
import com.example.personnel_service.event.UserRegisteredEvent;
import com.example.personnel_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final PersonRepository personRepository;

    @RabbitListener(queues = RabbitMQConfig.TASK_ASSIGNED_QUEUE)
    public void handleTaskAssigned(TaskAssignedEvent event) {
        try {
            log.info("Received task.assigned event: taskId={}, assignedTo={}, priority={}",
                    event.getPayload().getTaskId(),
                    event.getPayload().getAssignedTo(),
                    event.getPayload().getPriority());

            Long personnelId = extractPersonnelId(event.getPayload().getAssignedTo());
            personRepository.findById(personnelId).ifPresentOrElse(person -> {
                person.setStatus("ON_DUTY");
                personRepository.save(person);
                log.info("Updated personnel {} status to ON_DUTY for task {}",
                        personnelId, event.getPayload().getTaskId());
            }, () -> log.warn("Personnel not found for ID: {}", personnelId));

        } catch (Exception e) {
            log.error("Failed to process task.assigned event: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.USER_REGISTERED_QUEUE)
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            log.info("Received user.registered event: userId={}, username={}",
                    event.getPayload().getUserId(),
                    event.getPayload().getUsername());

            String fullName = event.getPayload().getFullName();
            String firstName = "";
            String lastName = "";
            if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.trim().split("\\s+", 2);
                firstName = parts[0];
                lastName = parts.length > 1 ? parts[1] : "";
            }

            Person person = new Person();
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setEmail(event.getPayload().getEmail());
            person.setPhone(event.getPayload().getPhoneNumber());
            person.setRole(event.getPayload().getRole() != null ? event.getPayload().getRole() : "VOLUNTEER");
            person.setStatus("AVAILABLE");
            person.setDisabled(false);

            personRepository.save(person);
            log.info("Created Person record (id={}) from user.registered event for user: {}",
                    person.getId(), event.getPayload().getUsername());

        } catch (Exception e) {
            log.error("Failed to process user.registered event: {}", e.getMessage(), e);
        }
    }

    private Long extractPersonnelId(String personnelCode) {
        try {
            String numericPart = personnelCode.replaceAll("[^0-9]", "");
            return Long.parseLong(numericPart);
        } catch (Exception e) {
            log.error("Failed to extract personnel ID from code: {}", personnelCode, e);
            throw new IllegalArgumentException("Invalid personnel code format: " + personnelCode);
        }
    }
}
