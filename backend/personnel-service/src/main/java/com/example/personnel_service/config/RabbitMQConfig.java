package com.example.personnel_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for the personnel service.
 * 
 * <p>This configuration sets up exchanges, queues, and bindings for inter-service
 * communication with the task-service. It enables asynchronous messaging for
 * task assignments and personnel status updates.
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@Configuration
public class RabbitMQConfig {
    
    /**
     * Name of the main topic exchange for disaster management events.
     */
    public static final String EXCHANGE_NAME = "disaster.topic.exchange";
    
    // Queue names
    /**
     * Queue for receiving task assignment events from task-service.
     */
    public static final String TASK_ASSIGNED_QUEUE = "personnel.task.assigned.queue";
    
    /**
     * Queue for personnel status change events.
     */
    public static final String PERSONNEL_STATUS_QUEUE = "personnel.status.queue";
    
    // Routing keys
    /**
     * Routing key for task assignment events.
     */
    public static final String TASK_ASSIGNED_KEY = "task.assigned";
    
    /**
     * Routing key for personnel status change events.
     */
    public static final String PERSONNEL_STATUS_KEY = "personnel.status.changed";
    
    /**
     * Routing key for personnel availability events.
     */
    public static final String PERSONNEL_AVAILABLE_KEY = "personnel.available";
    
    /**
     * Creates the main topic exchange for disaster management events.
     * 
     * @return TopicExchange instance for routing messages based on routing keys
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    /**
     * Creates queue for receiving task assignment notifications.
     * 
     * @return Durable queue that persists messages across RabbitMQ restarts
     */
    @Bean
    public Queue taskAssignedQueue() {
        return new Queue(TASK_ASSIGNED_QUEUE, true);
    }
    
    /**
     * Creates queue for personnel status updates.
     * 
     * @return Durable queue for status change events
     */
    @Bean
    public Queue personnelStatusQueue() {
        return new Queue(PERSONNEL_STATUS_QUEUE, true);
    }
    
    /**
     * Binds the task assigned queue to the exchange with routing key.
     * 
     * @return Binding that routes task.assigned events to personnel service
     */
    @Bean
    public Binding taskAssignedBinding() {
        return BindingBuilder
            .bind(taskAssignedQueue())
            .to(exchange())
            .with(TASK_ASSIGNED_KEY);
    }
    
    /**
     * Binds the personnel status queue to the exchange.
     * 
     * @return Binding for personnel status change events
     */
    @Bean
    public Binding personnelStatusBinding() {
        return BindingBuilder
            .bind(personnelStatusQueue())
            .to(exchange())
            .with(PERSONNEL_STATUS_KEY);
    }
    
    /**
     * Creates JSON message converter for automatic serialization/deserialization.
     * 
     * @return Jackson2JsonMessageConverter for converting Java objects to JSON messages
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * Configures RabbitTemplate with JSON message converter.
     * 
     * @param connectionFactory The RabbitMQ connection factory
     * @return RabbitTemplate configured for sending messages
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
