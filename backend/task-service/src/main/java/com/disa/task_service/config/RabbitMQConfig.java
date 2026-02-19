/*
 * RabbitMQConfig declares the RabbitMQ infrastructure beans for the task service,
 * including the topic exchange, durable queues, bindings, and a Jackson-based
 * message converter for JSON serialization of all event payloads.
 */
package com.disa.task_service.config;

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
    public static final String ASSESSMENT_COMPLETED_QUEUE = "assessment.completed.queue";
    public static final String TASK_ASSIGNED_QUEUE = "task.assigned.queue";

    // Routing keys
    public static final String ASSESSMENT_COMPLETED_KEY = "assessment.completed";
    public static final String TASK_ASSIGNED_KEY = "task.assigned";

    /**
     * Declares the shared topic exchange used by all disaster microservices.
     *
     * @return the configured {@link TopicExchange}
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Declares the durable queue that receives {@code assessment.completed} events.
     *
     * @return the assessment completed {@link Queue}
     */
    @Bean
    public Queue assessmentCompletedQueue() {
        return new Queue(ASSESSMENT_COMPLETED_QUEUE, true);
    }

    /**
     * Declares the durable queue that receives {@code task.assigned} events.
     *
     * @return the task assigned {@link Queue}
     */
    @Bean
    public Queue taskAssignedQueue() {
        return new Queue(TASK_ASSIGNED_QUEUE, true);
    }

    /**
     * Binds the assessment completed queue to the topic exchange with the
     * {@code assessment.completed} routing key.
     *
     * @return the {@link Binding} between the queue and the exchange
     */
    @Bean
    public Binding assessmentBinding() {
        return BindingBuilder
            .bind(assessmentCompletedQueue())
            .to(exchange())
            .with(ASSESSMENT_COMPLETED_KEY);
    }

    /**
     * Binds the task assigned queue to the topic exchange with the
     * {@code task.assigned} routing key.
     *
     * @return the {@link Binding} between the queue and the exchange
     */
    @Bean
    public Binding taskBinding() {
        return BindingBuilder
            .bind(taskAssignedQueue())
            .to(exchange())
            .with(TASK_ASSIGNED_KEY);
    }

    /**
     * Creates a Jackson-based message converter that serializes and deserializes
     * all RabbitMQ message payloads as JSON.
     *
     * @return the {@link Jackson2JsonMessageConverter}
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the {@link RabbitTemplate} with the Jackson JSON message converter
     * so that all outbound messages are serialized to JSON automatically.
     *
     * @param connectionFactory the RabbitMQ connection factory
     * @return the configured {@link RabbitTemplate}
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}