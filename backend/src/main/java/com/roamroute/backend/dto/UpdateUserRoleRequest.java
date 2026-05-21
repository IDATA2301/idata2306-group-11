package com.roamroute.backend.dto;

/**
 * Request payload for updating a user's role (admin, user, etc.).
 */
public class UpdateUserRoleRequest {
  private String role;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
