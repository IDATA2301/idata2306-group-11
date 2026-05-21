package com.roamroute.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
/**
 * Provides password encoder beans used by authentication logic.
 *
 * <p>The application uses BCrypt for hashing passwords; exposing a {@link PasswordEncoder}
 * bean keeps the hashing implementation in one place and makes it easy to replace
 * in tests or future upgrades.
 */
public class PasswordEncoderConfig {

  /**
   * BCrypt-based {@link PasswordEncoder} used for hashing user passwords.
   *
   * @return a {@link BCryptPasswordEncoder} instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
