package com.disa.logistic_service.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger/OpenAPI 3.0.
 * Accessible at: http://localhost:8086/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI logisticsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Logistics Service API")
                        .description("API for managing disaster response missions, vehicle fleets, and resource delivery.")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}