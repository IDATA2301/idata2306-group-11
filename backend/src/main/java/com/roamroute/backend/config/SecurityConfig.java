// package com.roamroute.backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//   @Bean
//   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//     http
//       .csrf(csrf -> csrf.disable())
//       .authorizeHttpRequests(auth -> auth

//         // Public endpoints
//         .requestMatchers("/api/auth/**").permitAll()
//         .requestMatchers("/api/trips/**").permitAll()
        
//         // Admin only
//         .requestMatchers("/api/admin/**").hasRole("ADMIN")
        
//         //Everything else requires authentication
//         .anyRequest().authenticated()
//       )
//       .httpBasic();  // Temporary, for testing purposes. Replace with JWT or similar in production.

//     return http.build();
//   }

//   @Bean
//   public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
//     return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance(); // For testing only. Use a real password encoder in production!  
//   }

// }
