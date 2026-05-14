package com.roamroute.backend.dto;

public class DestinationDTO {

  private int id;
  private String city;
  private String country;
  private String imageUrl;
  private String imageAlt;

  public DestinationDTO(int id, String city, String country, String imageUrl, String imageAlt) {
    this.id = id;
    this.city = city;
    this.country = country;
    this.imageUrl = imageUrl;
    this.imageAlt = imageAlt;
  }

  public int getId() {
    return id;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getImageAlt() {
    return imageAlt;
  }
}
