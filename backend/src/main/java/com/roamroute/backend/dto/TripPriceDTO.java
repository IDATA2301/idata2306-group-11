package com.roamroute.backend.dto;

/**
 * DTO representing a pricing option for a trip, including provider information and airline details if applicable.
 */
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
