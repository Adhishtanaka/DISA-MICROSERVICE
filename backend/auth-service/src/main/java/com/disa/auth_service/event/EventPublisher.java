package com.disa.auth_service.event;

import com.disa.auth_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserRegistered(UserRegisteredEvent event) {
        try {
            log.info("Publishing user.registered event: userId={}, username={}",
                    event.getPayload().getUserId(),
                    event.getPayload().getUsername());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.USER_REGISTERED_KEY,
                    event
            );

            log.info("user.registered event published successfully");
        } catch (Exception e) {
            log.error("Failed to publish user.registered event: {}", e.getMessage(), e);
        }
    }
}
