package com.roamroute.backend.dto;

import java.math.BigDecimal;

/**
 * Request for adding or updating a flight pricing option.
 *
 * <p>Contains the flight reference, a pricing provider name and price. Used when
 * an admin creates or modifies flight options for a trip.
 */
public class FlightOptionRequest {

  private Integer flightId;
  private String provider;
  private BigDecimal price;

  public Integer getFlightId() {
    return flightId;
  }

  public void setFlightId(Integer flightId) {
    this.flightId = flightId;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
