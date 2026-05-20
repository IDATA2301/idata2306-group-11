package com.roamroute.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  @Column(name = "user_name", unique = true, nullable = false, length = 20)
  private String user_name;

  @Column(name = "full_name", length = 100)
  private String full_name;

  @Column(unique = true)
  private String email;

  private String user_password;

  private String user_role;

  private String user_address;

  private String user_country;

  @Column(name = "password_reset_token")
  private String passwordResetToken;

  @Column(name = "password_reset_token_expiry")
  private Long passwordResetTokenExpiry;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUser_password() {
    return user_password;
  }

  public void setUser_password(String user_password) {
    this.user_password = user_password;
  }

  public String getUser_role() {
    return user_role;
  }

  public void setUser_role(String user_role) {
    this.user_role = user_role;
  }

  public String getUser_address() {
    return user_address;
  }

  public void setUser_address(String user_address) {
    this.user_address = user_address;
  }

  public String getUser_country() {
    return user_country;
  }

  public void setUser_country(String user_country) {
    this.user_country = user_country;
  }

  public String getPasswordResetToken() {
    return passwordResetToken;
  }

  public void setPasswordResetToken(String passwordResetToken) {
    this.passwordResetToken = passwordResetToken;
  }

  public Long getPasswordResetTokenExpiry() {
    return passwordResetTokenExpiry;
  }

  public void setPasswordResetTokenExpiry(Long passwordResetTokenExpiry) {
    this.passwordResetTokenExpiry = passwordResetTokenExpiry;
  }

}
