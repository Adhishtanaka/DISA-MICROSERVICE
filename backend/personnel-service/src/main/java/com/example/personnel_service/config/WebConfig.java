package com.example.personnel_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web configuration for the Personnel Service.
 * Configures CORS (Cross-Origin Resource Sharing) policies to allow frontend applications
 * to access the API from different domains.
 *
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders(
                        "Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "X-Requested-With"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));
        config.setAllowedHeaders(List.of(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Requested-With"
        ));
        config.setExposedHeaders(List.of(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"
        ));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
