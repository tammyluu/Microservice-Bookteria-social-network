package com.tammy.identityservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerApiConfig {
    //integration domain in doc
    @Bean
    public GroupedOpenApi publicApi(@Value("${swaggerApi.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service
                .packagesToScan("com.tammy.identityservice.controller") // scan all controller dans this package
                .build();
    }

    @Bean
    public OpenAPI swaggerApi(
            @Value("${swaggerApi.service.title}") String title,
            @Value("${swaggerApi.service.version}") String version,
            @Value("${swaggerApi.service.server}") String serverUrl) {
        return new OpenAPI()
                //setup list of url for endpoint
                .servers(List.of(new Server().url(serverUrl)))
                .info(new Info().title(title)
                        .description("API Authentication documents")
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));

    }
}
