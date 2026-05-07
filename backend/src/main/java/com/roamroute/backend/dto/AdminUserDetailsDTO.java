package com.roamroute.backend.dto;

public class AdminUserDetailsDTO {
  private int id;
  private String user_name;
  private String email;
  private String user_role;

  public AdminUserDetailsDTO(int id, String user_name, String email, String user_role) {
    this.id = id;
    this.user_name = user_name;
    this.email = email;
    this.user_role = user_role;
  }

  public int getId() {
    return id;
  }

  public String getUser_name() {
    return user_name;
  }

  public String getEmail() {
    return email;
  }

  public String getUser_role() {
    return user_role;
  }

  
}
