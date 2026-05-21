package com.roamroute.backend.dto;

/**
 * Request payload for updating a user's username.
 */
public class UpdateUsernameRequest {

  private String userName;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
