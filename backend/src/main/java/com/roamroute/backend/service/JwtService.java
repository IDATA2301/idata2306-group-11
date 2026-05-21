package com.roamroute.backend.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
/**
 * Service for generating and parsing JWT tokens with email and role claims for user authentication.
 */
public class JwtService {

  private final SecretKey signingKey;
  private final long expirationMs;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-ms}") long expirationMs) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(String email, String role) {
    Date now = new Date();
    return Jwts.builder()
        .subject(email)
        .claim("role", role)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expirationMs))
        .signWith(signingKey, Jwts.SIG.HS256)
        .compact();
  }

  public Claims parse(String token) throws JwtException {
    return Jwts.parser()
        .verifyWith(signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
