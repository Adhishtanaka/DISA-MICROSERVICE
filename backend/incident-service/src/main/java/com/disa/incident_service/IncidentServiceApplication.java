/*
 * Incident Service Application
 *
 * This is the main application class for the Incident Service microservice.
 * It bootstraps the Spring Boot application and enables auto-configuration.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IncidentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncidentServiceApplication.class, args);
	}

}
