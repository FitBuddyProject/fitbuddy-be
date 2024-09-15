package com.fitbuddy.service.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

@Configuration(value = "swagger_configuration")
@Profile(value = {"local"})
@RequiredArgsConstructor
public class SwaggerConfiguration {
    private final Environment environment;
    private final String swagger = "swagger";
    private final String TITLE = String.format("%s.title", swagger);
    private final String DESCRIPTION = String.format("%s.description", swagger);
    private final String VERSION = String.format("%s.version", swagger);


    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private Components getComponents() {
        return new Components().addSecuritySchemes("Authorization", this.getSecurityScheme());
    }

    @Bean
    public OpenAPI api() {
        SecurityRequirement addSecurityItem = new SecurityRequirement();


        addSecurityItem.addList("Authorization");
        return new OpenAPI()
                .components(this.getComponents())
                .addSecurityItem(addSecurityItem)
                .info(apiInfo());
    }



    private Info apiInfo() {
        return new Info()
                .title(environment.getProperty(TITLE))
                .description(environment.getProperty(DESCRIPTION))
                .version(environment.getProperty(VERSION));
    }

}
