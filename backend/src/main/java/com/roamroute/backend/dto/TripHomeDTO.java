package com.roamroute.backend.dto;

import java.sql.Date;

/**
 * DTO for displaying a trip summary on the home/listing page with essential trip details and lowest available price.
 */
public class TripHomeDTO {
  private int id;
  private String title;
  private String imageUrl;
  private String city;
  private String country;
  private double lowestPrice;
  private Date startDate;
  private Date endDate;
  private boolean active;

  public TripHomeDTO(int id, String title, String imageUrl, String city, String country, double lowestPrice, Date startDate, Date endDate, boolean active) {
    this.id = id;
    this.title = title;
    this.imageUrl = imageUrl;
    this.city = city;
    this.country = country;
    this.lowestPrice = lowestPrice;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = active;
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

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public boolean isActive() {
    return active;
  }


}
