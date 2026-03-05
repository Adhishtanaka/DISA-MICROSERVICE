/**
 * RabbitMQConfig.java
 *
 * Configuration class for RabbitMQ messaging infrastructure used by the Shelter Service.
 * Defines the topic exchange, queues, bindings, and message converters required for
 * event-driven communication with other DISA microservices.
 *
 * This configuration is conditionally loaded only when the property
 * "spring.rabbitmq.enabled" is set to "true", allowing the service to run
 * without RabbitMQ in environments where messaging is not required.
 *
 * Exchange  : disaster.topic.exchange (TopicExchange)
 * Queue     : incident.created.queue
 * Routing   : incident.created
 */
package com.disa.shelter_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "disaster.topic.exchange";

    // Queue names — service-specific to avoid competing consumers with mission-service
    public static final String INCIDENT_CREATED_QUEUE = "shelter.incident.created.queue";

    // Routing keys
    public static final String INCIDENT_CREATED_KEY = "incident.created";

    @Value("${spring.rabbitmq.host:localhost}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port:5672}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username:guest}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password:guest}")
    private String rabbitPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        factory.setUsername(rabbitUsername);
        factory.setPassword(rabbitPassword);
        factory.getRabbitConnectionFactory().setConnectionTimeout(5000);
        factory.getRabbitConnectionFactory().setRequestedHeartbeat(30);
        return factory;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue incidentCreatedQueue() {
        return new Queue(INCIDENT_CREATED_QUEUE, true);
    }

    @Bean
    public Binding incidentCreatedBinding() {
        return BindingBuilder
                .bind(incidentCreatedQueue())
                .to(exchange())
                .with(INCIDENT_CREATED_KEY);
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
