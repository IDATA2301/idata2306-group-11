package com.roamroute.backend.dto;

public class TripPriceDTO {
  private int id;
  private String provider;
  private double price;
  private String airline;

  public TripPriceDTO(int id, String provider, double price) {
    this(id, provider, price, null);
  }

  public TripPriceDTO(int id, String provider, double price, String airline) {
    this.id = id;
    this.provider = provider;
    this.price = price;
    this.airline = airline;
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

  public String getAirline() {
    return airline;
  }
}
