package com.disa.mission_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration defining Queues, Exchanges, and Bindings.
 * Sets up JSON message conversion for easy DTO handling.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "disaster.topic.exchange";

    // Queue Names
    public static final String INCIDENT_CREATED_QUEUE = "incident.created.queue";
    public static final String INCIDENT_ESCALATED_QUEUE = "incident.escalated.queue";
    public static final String RESOURCE_LOW_QUEUE = "resource.critical_low.queue";

    // Routing Keys
    public static final String INCIDENT_CREATED_KEY = "incident.created";
    public static final String INCIDENT_ESCALATED_KEY = "incident.escalated";
    public static final String RESOURCE_LOW_KEY = "resource.critical_low";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // --- Queues ---
    @Bean
    public Queue incidentCreatedQueue() {
        return new Queue(INCIDENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue incidentEscalatedQueue() {
        return new Queue(INCIDENT_ESCALATED_QUEUE, true);
    }

    @Bean
    public Queue resourceLowQueue() {
        return new Queue(RESOURCE_LOW_QUEUE, true);
    }

    // --- Bindings ---
    @Bean
    public Binding incidentCreatedBinding() {
        return BindingBuilder.bind(incidentCreatedQueue()).to(exchange()).with(INCIDENT_CREATED_KEY);
    }

    @Bean
    public Binding incidentEscalatedBinding() {
        return BindingBuilder.bind(incidentEscalatedQueue()).to(exchange()).with(INCIDENT_ESCALATED_KEY);
    }

    @Bean
    public Binding resourceLowBinding() {
        return BindingBuilder.bind(resourceLowQueue()).to(exchange()).with(RESOURCE_LOW_KEY);
    }

    // --- Converters ---
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}