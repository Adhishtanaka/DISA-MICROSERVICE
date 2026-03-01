/**
 * Main application class for the Resource Service microservice.
 * This Spring Boot application provides CRUD operations for disaster relief resources,
 * inventory management, and critical stock level monitoring with event publishing.
 */
package com.disa.resource_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServiceApplication.class, args);
	}

}
