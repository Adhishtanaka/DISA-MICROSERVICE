/**
 * ShelterServiceApplication.java
 *
 * Entry point for the Shelter Service microservice.
 * This Spring Boot application manages emergency shelters within the DISA
 * (Disaster Information and Support Application) platform. It handles shelter
 * registration, occupancy tracking, status management, and event-driven
 * integration with other microservices via RabbitMQ.
 *
 * RabbitAutoConfiguration is excluded to allow conditional RabbitMQ setup
 * controlled by the application properties.
 */
package com.disa.shelter_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
public class ShelterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterServiceApplication.class, args);
    }
}
