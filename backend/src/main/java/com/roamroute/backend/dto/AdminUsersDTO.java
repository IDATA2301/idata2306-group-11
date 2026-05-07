package com.roamroute.backend.dto;

public class AdminUsersDTO {
  private int id;
  private String username;
  private String email;
  private String user_role;

  public AdminUsersDTO(int id, String username, String email, String user_role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.user_role = user_role;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getUser_role() {
    return user_role;
  }

}
