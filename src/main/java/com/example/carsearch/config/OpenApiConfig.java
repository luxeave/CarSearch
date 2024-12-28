package com.example.carsearch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Search API")
                        .description("REST API for searching and managing car information")
                        .version("1.0"));
    }
}