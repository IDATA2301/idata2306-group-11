package com.roamroute.backend.controller;

import java.text.Normalizer;

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
import com.roamroute.backend.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "User registration, login and profile management")
public class AuthController {

  private static final int MIN_USERNAME_LENGTH = 6;
  private static final int MAX_USERNAME_LENGTH = 20;
  private static final int MIN_FULL_NAME_LENGTH = 2;
  private static final int MAX_FULL_NAME_LENGTH = 100;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  @SecurityRequirements
  @Operation(summary = "Register a new user account and return a JWT")
  @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
  public LoginResponse register(@RequestBody RegisterRequest request) {
    if (!StringUtils.hasText(request.getFullName())
      || !StringUtils.hasText(request.getEmail())
      || !StringUtils.hasText(request.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
    }

    String normalizedEmail = request.getEmail().trim().toLowerCase();

    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
    }

    String normalizedFullName = validateFullName(request.getFullName());
    String generatedUserName = generateUniqueUsername(normalizedFullName);

    User user = new User();
    user.setUser_name(generatedUserName);
    user.setFull_name(normalizedFullName);
    user.setEmail(normalizedEmail);
    user.setUser_password(passwordEncoder.encode(request.getPassword()));
    user.setUser_role("USER");
    user.setUser_address(StringUtils.hasText(request.getAddress()) ? request.getAddress().trim() : null);
    user.setUser_country(StringUtils.hasText(request.getCountry()) ? request.getCountry().trim() : null);

    User savedUser = userRepository.save(user);

    String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getUser_role());

    return new LoginResponse(
      savedUser.getId(),
      savedUser.getUser_name(),
      savedUser.getFull_name(),
      savedUser.getEmail(),
      savedUser.getUser_role(),
      token
    );
  }

  @PostMapping("/login")
  @SecurityRequirements
  @Operation(summary = "Authenticate with email + password and receive a JWT")
  public LoginResponse login(@RequestBody LoginRequest request) {
    if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
    }

    User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), user.getUser_password())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    String token = jwtService.generateToken(user.getEmail(), user.getUser_role());

    return new LoginResponse(
      user.getId(),
      user.getUser_name(),
      user.getFull_name(),
      user.getEmail(),
      user.getUser_role(),
      token
    );

  }

  @PutMapping("/profile/{id}/username")
  @Operation(summary = "Update the username of the authenticated user")
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

    String token = jwtService.generateToken(updatedUser.getEmail(), updatedUser.getUser_role());

    return new LoginResponse(
      updatedUser.getId(),
      updatedUser.getUser_name(),
      updatedUser.getFull_name(),
      updatedUser.getEmail(),
      updatedUser.getUser_role(),
      token
    );
  }

  private String validateFullName(String rawFullName) {
    String trimmed = rawFullName.trim().replaceAll("\\s+", " ");
    if (trimmed.length() < MIN_FULL_NAME_LENGTH || trimmed.length() > MAX_FULL_NAME_LENGTH) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Full name must be between " + MIN_FULL_NAME_LENGTH + " and " + MAX_FULL_NAME_LENGTH + " characters long"
      );
    }
    return trimmed;
  }

  private String generateUniqueUsername(String fullName) {
    String base = fullName.toLowerCase()
      .replace("æ", "ae")
      .replace("ø", "oe")
      .replace("å", "aa");
    base = Normalizer.normalize(base, Normalizer.Form.NFD)
      .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
      .replaceAll("[^a-z0-9]", "");

    if (base.isEmpty()) {
      base = "user";
    }
    if (base.length() > MAX_USERNAME_LENGTH - 4) {
      base = base.substring(0, MAX_USERNAME_LENGTH - 4);
    }
    StringBuilder padded = new StringBuilder(base);
    while (padded.length() < MIN_USERNAME_LENGTH) {
      padded.append("0");
    }
    base = padded.toString();

    String candidate = base;
    int suffix = 1;
    while (userRepository.existsByUserNameIgnoreCase(candidate)) {
      String suffixStr = String.valueOf(suffix);
      int maxBaseLen = MAX_USERNAME_LENGTH - suffixStr.length();
      String trimmedBase = base.length() > maxBaseLen ? base.substring(0, maxBaseLen) : base;
      candidate = trimmedBase + suffixStr;
      suffix++;
    }
    return candidate;
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
