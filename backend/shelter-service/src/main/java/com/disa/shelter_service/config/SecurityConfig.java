/**
 * SecurityConfig.java
 *
 * Spring Security configuration for the Shelter Service.
 * Configures the HTTP security filter chain for the application.
 *
 * Current configuration:
 * - Disables CSRF protection (suitable for stateless REST APIs)
 * - Permits all incoming HTTP requests without authentication
 *
 * Note: This open configuration is intended for internal microservice
 * communication within a trusted network. In production, consider
 * adding JWT-based authentication or API gateway-level security.
 */
package com.disa.shelter_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
