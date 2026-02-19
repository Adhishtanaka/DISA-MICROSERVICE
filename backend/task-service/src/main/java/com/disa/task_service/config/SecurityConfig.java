/*
 * SecurityConfig configures the Spring Security filter chain for the task service.
 * CSRF protection is disabled for stateless REST API usage. All requests are
 * permitted as authentication is handled at the API gateway level.
 */
package com.disa.task_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain with CSRF disabled and all requests permitted.
     * Authentication and authorization are delegated to the API gateway.
     *
     * @param http the {@link HttpSecurity} builder
     * @return the built {@link SecurityFilterChain}
     * @throws Exception if the security configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            );
        return http.build();
    }
}