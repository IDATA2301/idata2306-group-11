package com.roamroute.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.UserRepository;

@Service
/**
 * Spring Security UserDetailsService implementation that loads user authentication details by email from the database.
 */
public class CustomerUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomerUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    System.out.println("Loaded user: " + user.getEmail() + " with role: " + user.getUser_role());
    
    return org.springframework.security.core.userdetails.User
      .builder()
      .username(user.getEmail())
      .password(user.getUser_password())
      .roles(user.getUser_role())
      .build();
  }
}
