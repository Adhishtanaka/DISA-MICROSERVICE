package com.disa.resource_service.config;

import com.disa.resource_service.security.JwtAuthenticationFilter;
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
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                // Read — all authenticated
                .requestMatchers(HttpMethod.GET, "/api/resources/**").authenticated()
                // Decrement stock — ADMIN, COORDINATOR, RESPONDER (used when deploying resources)
                .requestMatchers(HttpMethod.PUT, "/api/resources/*/decrement").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                // Create — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.POST, "/api/resources").hasAnyRole("ADMIN", "COORDINATOR")
                // Update / increment / set stock — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.PUT, "/api/resources/**").hasAnyRole("ADMIN", "COORDINATOR")
                // Delete — ADMIN only
                .requestMatchers(HttpMethod.DELETE, "/api/resources/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
