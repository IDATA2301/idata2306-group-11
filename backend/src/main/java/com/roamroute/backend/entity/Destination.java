package com.roamroute.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "destinations")
public class Destination {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  private String city;
  private String country;
  private String image_url;
  private String image_alt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getImage_alt() {
    return image_alt;
  }

  public void setImage_alt(String image_alt) {
    this.image_alt = image_alt;
  }

}
