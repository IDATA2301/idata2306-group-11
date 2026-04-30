package com.roamroute.backend.dto;

public class LoginResponse {

  private int id;
  private String userName;
  private String fullName;
  private String email;
  private String role;
  private String token;

  public LoginResponse(int id, String userName, String fullName, String email, String role, String token) {
    this.id = id;
    this.userName = userName;
    this.fullName = fullName;
    this.email = email;
    this.role = role;
    this.token = token;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
