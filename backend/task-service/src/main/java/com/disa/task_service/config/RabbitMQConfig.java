/*
 * RabbitMQConfig for task service
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

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue assessmentCompletedQueue() {
        return new Queue(ASSESSMENT_COMPLETED_QUEUE, true);
    }

    @Bean
    public Queue taskAssignedQueue() {
        return new Queue(TASK_ASSIGNED_QUEUE, true);
    }

    @Bean
    public Binding assessmentBinding() {
        return BindingBuilder
            .bind(assessmentCompletedQueue())
            .to(exchange())
            .with(ASSESSMENT_COMPLETED_KEY);
    }

    @Bean
    public Binding taskBinding() {
        return BindingBuilder
            .bind(taskAssignedQueue())
            .to(exchange())
            .with(TASK_ASSIGNED_KEY);
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