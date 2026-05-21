package com.roamroute.backend.dto;

/**
 * Request payload for user login.
 *
 * <p>Contains email and password credentials that the backend validates against
 * stored user records.
 */
public class LoginRequest {

  private String email;
  private String password;


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
