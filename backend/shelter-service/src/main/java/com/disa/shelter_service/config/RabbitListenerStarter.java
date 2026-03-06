package com.disa.shelter_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Starts RabbitMQ listeners after the application is fully ready.
 * This prevents startup failure when RabbitMQ is temporarily unreachable —
 * auto-startup is disabled in application.yml and listeners are started here instead.
 */
@Component
public class RabbitListenerStarter {

    private static final Logger log = LoggerFactory.getLogger(RabbitListenerStarter.class);

    private final RabbitListenerEndpointRegistry registry;

    public RabbitListenerStarter(RabbitListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startListeners() {
        try {
            registry.start();
            log.info("RabbitMQ listeners started successfully");
        } catch (Exception e) {
            log.warn("RabbitMQ listeners could not start (will retry): {}", e.getMessage());
        }
    }
}
