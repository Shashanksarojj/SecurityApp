package com.example.securityapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SecurityApp API (v1)",
                version = "1.0",
                description = "JWT Auth, Refresh Token, RBAC, Pagination, Validation, Rate Limiting"
        )
)
public class OpenApiConfig {
}
