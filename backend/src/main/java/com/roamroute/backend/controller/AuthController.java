package com.roamroute.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.LoginRequest;
import com.roamroute.backend.dto.LoginResponse;
import com.roamroute.backend.dto.RegisterRequest;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
  public LoginResponse register(@RequestBody RegisterRequest request) {
    if (!StringUtils.hasText(request.getUserName())
      || !StringUtils.hasText(request.getEmail())
      || !StringUtils.hasText(request.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
    }

    String normalizedEmail = request.getEmail().trim().toLowerCase();

    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
    }

    User user = new User();
    user.setUser_name(request.getUserName().trim());
    user.setEmail(normalizedEmail);
    user.setUser_password(request.getPassword());
    user.setUser_role("USER");
    user.setUser_address(StringUtils.hasText(request.getAddress()) ? request.getAddress().trim() : null);
    user.setUser_country(StringUtils.hasText(request.getCountry()) ? request.getCountry().trim() : null);

    User savedUser = userRepository.save(user);

    return new LoginResponse(
      savedUser.getId(),
      savedUser.getUser_name(),
      savedUser.getEmail(),
      savedUser.getUser_role()
    );
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest request) {
    if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
    }

    User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase());

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    if (!user.getUser_password().equals(request.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return new LoginResponse(
      user.getId(),
      user.getUser_name(),
      user.getEmail(),
      user.getUser_role()
    );
    
  }
  

}
