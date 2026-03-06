package com.disa.assessment_service.config;

import com.disa.assessment_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                // Photo download — all authenticated
                .requestMatchers(HttpMethod.GET, "/api/assessments/**").authenticated()
                // Create / update / complete / upload — ADMIN, COORDINATOR, RESPONDER
                .requestMatchers(HttpMethod.POST, "/api/assessments").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                .requestMatchers(HttpMethod.POST, "/api/assessments/*/photos").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                .requestMatchers(HttpMethod.PUT, "/api/assessments/**").hasAnyRole("ADMIN", "COORDINATOR", "RESPONDER")
                // Delete — ADMIN, COORDINATOR
                .requestMatchers(HttpMethod.DELETE, "/api/assessments/**").hasAnyRole("ADMIN", "COORDINATOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
