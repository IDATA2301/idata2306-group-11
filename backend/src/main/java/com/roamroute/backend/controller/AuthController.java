package com.roamroute.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.LoginRequest;
import com.roamroute.backend.dto.LoginResponse;
import com.roamroute.backend.dto.RegisterRequest;
import com.roamroute.backend.dto.UpdateUsernameRequest;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final int MIN_USERNAME_LENGTH = 6;
  private static final int MAX_USERNAME_LENGTH = 20;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
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

    String normalizedUserName = validateAndNormalizeUserName(request.getUserName());

    if (userRepository.existsByUserNameIgnoreCase(normalizedUserName)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
    }

    User user = new User();
    user.setUser_name(normalizedUserName);
    user.setEmail(normalizedEmail);
    user.setUser_password(passwordEncoder.encode(request.getPassword()));
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

    if (!passwordEncoder.matches(request.getPassword(), user.getUser_password())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return new LoginResponse(
      user.getId(),
      user.getUser_name(),
      user.getEmail(),
      user.getUser_role()
    );
    
  }

  @PutMapping("/profile/{id}/username")
  public LoginResponse updateUsername(@PathVariable int id, @RequestBody UpdateUsernameRequest request) {
    if (!StringUtils.hasText(request.getUserName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
    }

    String normalizedUserName = validateAndNormalizeUserName(request.getUserName());

    if (userRepository.existsByUserNameIgnoreCaseAndIdNot(normalizedUserName, id)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
    }

    User user = userRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    user.setUser_name(normalizedUserName);
    User updatedUser = userRepository.save(user);

    return new LoginResponse(
      updatedUser.getId(),
      updatedUser.getUser_name(),
      updatedUser.getEmail(),
      updatedUser.getUser_role()
    );
  }

  private String validateAndNormalizeUserName(String rawUserName) {
    if (!StringUtils.hasText(rawUserName)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
    }

    if (rawUserName.chars().anyMatch(Character::isWhitespace)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot contain whitespace");
    }

    String normalizedUserName = rawUserName.trim();
    if (normalizedUserName.length() < MIN_USERNAME_LENGTH || normalizedUserName.length() > MAX_USERNAME_LENGTH) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be between 6 and 20 characters long");
    }

    return normalizedUserName;
  }
  

}
