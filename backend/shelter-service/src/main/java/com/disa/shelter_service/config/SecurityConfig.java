package com.disa.shelter_service.config;

import com.disa.shelter_service.security.JwtAuthenticationFilter;
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
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/webjars/**"
                ).permitAll()
                // Read — all authenticated
                .requestMatchers(HttpMethod.GET, "/api/shelters/**").authenticated()
                // Check-in / check-out — all authenticated roles
                .requestMatchers(HttpMethod.POST, "/api/shelters/*/checkin").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/shelters/*/checkout").authenticated()
                // Create — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.POST, "/api/shelters").hasAnyRole("ADMIN", "COORDINATOR")
                // Update / status — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.PUT, "/api/shelters/**").hasAnyRole("ADMIN", "COORDINATOR")
                // Delete — ADMIN only
                .requestMatchers(HttpMethod.DELETE, "/api/shelters/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
