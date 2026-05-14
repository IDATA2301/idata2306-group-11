package com.roamroute.backend.dto;

import java.math.BigDecimal;

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
