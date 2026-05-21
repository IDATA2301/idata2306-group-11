package com.roamroute.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Application CORS configuration.
 *
 * <p>Defines allowed origins, methods and headers and exposes a {@link CorsConfigurationSource}
 * bean that registers these rules under the `/api/**` path. The allowed origins are read
 * from the `app.cors.allowed-origins` property so they can be adjusted per-environment.
 */
@Configuration
public class CorsConfig {

  private final String[] allowedOrigins;

  public CorsConfig(@Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}") String[] allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  /**
   * Create and expose a {@link CorsConfigurationSource} used by Spring Security / MVC.
   *
   * @return configured CORS source that applies to `/api/**` endpoints
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(allowedOrigins));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
  }
}
