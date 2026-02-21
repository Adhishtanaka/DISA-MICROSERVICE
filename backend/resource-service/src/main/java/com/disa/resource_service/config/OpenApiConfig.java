package com.disa.resource_service.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger/OpenAPI 3.0.
 * Accessible at: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resourceServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Resource Service API")
                        .description("API for managing disaster response resources, inventory, and stock updates.")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}