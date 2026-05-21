package com.roamroute.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * OpenAPI / Swagger configuration for the RoamRoute backend.
 *
 * <p>Registers a bearer JWT security scheme and applies it globally so endpoints
 * in the generated documentation show the authentication requirement.
 */
public class OpenApiConfig {

  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI roamRouteOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("RoamRoute API")
            .description("API documentation for the RoamRoute backend")
            .version("1.0.0"))
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .components(new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
        // Public / user-facing tags first, admin tags last.
        // Swagger UI displays tags in spec order when tagsSorter is not set.
        .addTagsItem(new Tag().name("Auth"))
        .addTagsItem(new Tag().name("Trips"))
        .addTagsItem(new Tag().name("Destinations"))
        .addTagsItem(new Tag().name("Favorites"))
        .addTagsItem(new Tag().name("Orders"))
        .addTagsItem(new Tag().name("Contact"))
        .addTagsItem(new Tag().name("Admin / Users"))
        .addTagsItem(new Tag().name("Admin / Orders"))
        .addTagsItem(new Tag().name("Admin / Trips"))
        .addTagsItem(new Tag().name("Admin / Trip Options"))
        .addTagsItem(new Tag().name("Admin / Accommodations"))
        .addTagsItem(new Tag().name("Admin / Flights"))
        .addTagsItem(new Tag().name("Admin / Destinations"))
        .addTagsItem(new Tag().name("Admin / Uploads"));
  }
}
