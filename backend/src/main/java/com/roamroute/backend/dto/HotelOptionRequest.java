package com.roamroute.backend.dto;

import java.math.BigDecimal;

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
