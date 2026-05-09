package com.example.sovaibackend.common.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for environment variable binding
 * Spring Boot automatically expands ${VAR_NAME} placeholders from environment variables
 */
@Configuration
public class EnvConfig {
    // This configuration allows Spring Boot to resolve environment variables
    // in application.yml and other property files using the ${VAR_NAME} syntax
}


