package com.roamroute.backend.config;


import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth

        // CORS preflight
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // Public endpoints
        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/destinations/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

        // API documentation
        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

        // Error handling
        .requestMatchers("/error", "/error/**").permitAll()

        // Admin only
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/contact", "/api/contact/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/contact/**").hasRole("ADMIN")

        // Authenticated endpoints
        .requestMatchers("/api/orders/**").authenticated()
        .requestMatchers("/api/favorites/**").authenticated()
        .requestMatchers("/api/auth/profile/**").authenticated()

        // Safe default: everything else requires auth
        .anyRequest().authenticated()
      )
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
      )
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
