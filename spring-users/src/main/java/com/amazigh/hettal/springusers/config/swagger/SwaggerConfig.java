package com.amazigh.hettal.springusers.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
        .info(new Info()
            .title("API Docs")
            .description("API to manage customers")
            .version("1.0")
            .contact(new Contact()
                .email("test@gmail.com")
                .name("Amazigh")
                .url("amazigh.co")
            )
        ).addSecurityItem(new SecurityRequirement()
            .addList(securitySchemeName)
        ).components(new Components()
            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
            )
        ); // Add your API paths here dynamically if needed
  }
}
