package com.roamroute.backend.dto;

/**
 * Request payload for user registration.
 *
 * <p>Contains required registration fields (full name, email, password) and optional
 * profile fields (address, country).
 */
public class RegisterRequest {

  private String fullName;
  private String email;
  private String password;
  private String address;
  private String country;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
