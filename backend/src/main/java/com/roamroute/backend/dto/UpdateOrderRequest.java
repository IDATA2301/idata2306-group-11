package com.roamroute.backend.dto;

import java.math.BigDecimal;

public class UpdateOrderRequest {
  private int tripId;
  private Integer flightId;
  private Integer accommodationId;
  private String status;
  private BigDecimal totalPrice;

  public int getTripId() { return tripId; }
  public void setTripId(int tripId) { this.tripId = tripId; }

  public Integer getFlightId() { return flightId; }
  public void setFlightId(Integer flightId) { this.flightId = flightId; }

  public Integer getAccommodationId() { return accommodationId; }
  public void setAccommodationId(Integer accommodationId) { this.accommodationId = accommodationId; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public BigDecimal getTotalPrice() { return totalPrice; }
  public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
