package com.roamroute.backend.dto;

/**
 * Request payload for updating destination information including city, country, and associated imagery.
 */
public class UpdateDestinationRequest {

  private String city;
  private String country;
  private String imageUrl;
  private String imageAlt;

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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getImageAlt() {
    return imageAlt;
  }

  public void setImageAlt(String imageAlt) {
    this.imageAlt = imageAlt;
  }
}
