/*
 * Incident Service Application
 *
 * This is the main Spring Boot application class for the Incident Service.
 * It serves as the entry point for the microservice that handles disaster incident management.
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
