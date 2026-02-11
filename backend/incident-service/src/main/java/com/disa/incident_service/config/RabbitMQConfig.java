/*
 * RabbitMQ Configuration
 *
 * Configures RabbitMQ connection, queues, exchanges, and bindings for event publishing.
 * Sets up topic exchange and queues for incident events.
 */

package com.disa.incident_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "disaster.topic.exchange";

    // Queue names
    public static final String INCIDENT_CREATED_QUEUE = "incident.created.queue";
    public static final String INCIDENT_ESCALATED_QUEUE = "incident.escalated.queue";

    // Routing keys
    public static final String INCIDENT_CREATED_KEY = "incident.created";
    public static final String INCIDENT_ESCALATED_KEY = "incident.escalated";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue incidentCreatedQueue() {
        return new Queue(INCIDENT_CREATED_QUEUE);
    }

    @Bean
    public Queue incidentEscalatedQueue() {
        return new Queue(INCIDENT_ESCALATED_QUEUE);
    }

    @Bean
    public Binding incidentCreatedBinding(Queue incidentCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(incidentCreatedQueue).to(exchange).with(INCIDENT_CREATED_KEY);
    }

    @Bean
    public Binding incidentEscalatedBinding(Queue incidentEscalatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(incidentEscalatedQueue).to(exchange).with(INCIDENT_ESCALATED_KEY);
    }

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