package com.disa.task_service.config;

import com.disa.task_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Read — all authenticated
                .requestMatchers(HttpMethod.GET, "/tasks/**").authenticated()
                // Create — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.POST, "/tasks").hasAnyRole("ADMIN", "COORDINATOR")
                // Complete — ADMIN, COORDINATOR, RESPONDER
                .requestMatchers(HttpMethod.PUT, "/tasks/*/complete").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                // Assign / update — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.PUT, "/tasks/**").hasAnyRole("ADMIN", "COORDINATOR")
                // Delete — ADMIN only
                .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
