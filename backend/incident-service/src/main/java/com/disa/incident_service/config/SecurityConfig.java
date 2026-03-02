package com.disa.incident_service.config;

import com.disa.incident_service.security.JwtAuthenticationFilter;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Read access — all authenticated roles
                .requestMatchers(HttpMethod.GET, "/api/incidents/**").authenticated()
                // Create — ADMIN, COORDINATOR, RESPONDER
                .requestMatchers(HttpMethod.POST, "/api/incidents").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                // Update / escalate / status — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.PUT, "/api/incidents/**").hasAnyRole("ADMIN", "COORDINATOR")
                // Delete — ADMIN only
                .requestMatchers(HttpMethod.DELETE, "/api/incidents/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
