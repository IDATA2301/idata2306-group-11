package com.roamroute.backend.dto;

public class CreateOrderRequest {
  private int tripId;
  private int flightPriceId;
  private int accommodationPriceId;

  public int getTripId() {
    return tripId;
  }

  public void setTripId(int tripId) {
    this.tripId = tripId;
  }

  public int getFlightPriceId() {
    return flightPriceId;
  }

  public void setFlightPriceId(int flightId) {
    this.flightPriceId = flightId;
  }

  public int getAccommodationPriceId() {
    return accommodationPriceId;
  }

  public void setAccommodationPriceId(int accommodationId) {
    this.accommodationPriceId = accommodationId;
  }
}
