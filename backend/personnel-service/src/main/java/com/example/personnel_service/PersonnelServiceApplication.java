package com.example.personnel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Personnel Service microservice.
 * This service manages personnel-related operations including person profiles,
 * medical conditions, emergency contacts, skills, and assignments.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@SpringBootApplication
public class PersonnelServiceApplication {

	/**
	 * Main method to start the Personnel Service application.
	 * 
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(PersonnelServiceApplication.class, args);
	}

}
