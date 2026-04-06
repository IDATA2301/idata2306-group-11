package com.roamroute.backend.dto;

public class TripPriceDTO {
  private int id;
  private String provider;
  private double price;

  public TripPriceDTO(int id, String provider, double price) {
    this.id = id;
    this.provider = provider;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public String getProvider() {
    return provider;
  }

  public double getPrice() {
    return price;
  }
}
