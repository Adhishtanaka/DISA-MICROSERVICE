/**
 * SwaggerConfig.java
 *
 * Configuration class for Swagger / OpenAPI 3 documentation in the Shelter Service.
 * Uses springdoc-openapi to auto-generate interactive API documentation accessible
 * at /swagger-ui.html and the raw OpenAPI spec at /v3/api-docs.
 *
 * Provides metadata for the API including title, version, description, and
 * contact information displayed in the Swagger UI.
 */
package com.disa.shelter_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI shelterServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shelter Service API")
                        .description(
                                "REST API for managing emergency shelters in the DISA " +
                                "(Disaster Information and Support Application) platform. " +
                                "Provides endpoints for shelter registration, occupancy tracking, " +
                                "status management, and geographic proximity search."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DISA Team")
                                .email("support@disa.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
