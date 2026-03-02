package com.disa.mission_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web MVC configuration.
 * Mission endpoints are served at /missions (declared on MissionController).
 * The nginx reverse proxy routes /missions → mission-service:8086/missions.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
}
