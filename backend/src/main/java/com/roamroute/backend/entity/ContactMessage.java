package com.roamroute.backend.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contactmessages")
public class ContactMessage {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  int id;

  private String email;

  private String contactmessage_subject;

  private String contactmessage_message;

  private Date created_at;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getContactmessage_subject() {
    return contactmessage_subject;
  }

  public void setContactmessage_subject(String contactmessage_subject) {
    this.contactmessage_subject = contactmessage_subject;
  }

  public String getContactmessage_message() {
    return contactmessage_message;
  }

  public void setContactmessage_message(String contactmessage_message) {
    this.contactmessage_message = contactmessage_message;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

}
