package com.roamroute.backend.dto;

import java.math.BigDecimal;

/**
 * Request for adding or updating a hotel pricing option.
 *
 * <p>Contains the accommodation reference, pricing provider name and price. Used when
 * an admin creates or modifies hotel options for a trip.
 */
public class HotelOptionRequest {

  private Integer accommodationId;
  private String provider;
  private BigDecimal price;

  public Integer getAccommodationId() {
    return accommodationId;
  }

  public void setAccommodationId(Integer accommodationId) {
    this.accommodationId = accommodationId;
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
