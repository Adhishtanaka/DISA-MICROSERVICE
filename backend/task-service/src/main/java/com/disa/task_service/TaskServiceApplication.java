/*
 * TaskServiceApplication is the entry point for the DISA task-service microservice.
 * Bootstraps the Spring Boot application responsible for managing disaster response
 * tasks, including CRUD operations and event-driven integration via RabbitMQ.
 */
package com.disa.task_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskServiceApplication {

	/**
	 * Application entry point. Starts the embedded server and initializes
	 * all Spring context beans for the task-service.
	 *
	 * @param args command-line arguments passed to the JVM
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskServiceApplication.class, args);
	}

}
