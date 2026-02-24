package com.roamroute.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.LoginRequest;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public User login(@RequestBody LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail());

    if (user == null) {
      throw new RuntimeException("User not found");
    }

    if (!user.getEmail().equals(request.getEmail()) || !user.getUser_password().equals(request.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    return user;
    
  }
  

}
