package com.roamroute.backend.dto;


public class TripHomeDTO {
  private int id;
  private String title;
  private String imageUrl;
  private String city;
  private String country;
  private double lowestPrice;

  public TripHomeDTO(int id, String title, String imageUrl, String city, String country, double lowestPrice) {
    this.id = id;
    this.title = title;
    this.imageUrl = imageUrl;
    this.city = city;
    this.country = country;
    this.lowestPrice = lowestPrice;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public double getLowestPrice() {
    return lowestPrice;
  }


}
