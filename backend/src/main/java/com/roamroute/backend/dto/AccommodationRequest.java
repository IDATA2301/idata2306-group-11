package com.roamroute.backend.dto;

public class AccommodationRequest {

  private String hotelName;
  private String hotelType;
  private String hotelCity;
  private String hotelLocation;
  private String amenities;
  private Integer nights;
  private Double latitude;
  private Double longitude;

  public String getHotelName() {
    return hotelName;
  }

  public void setHotelName(String hotelName) {
    this.hotelName = hotelName;
  }

  public String getHotelType() {
    return hotelType;
  }

  public void setHotelType(String hotelType) {
    this.hotelType = hotelType;
  }

  public String getHotelCity() {
    return hotelCity;
  }

  public void setHotelCity(String hotelCity) {
    this.hotelCity = hotelCity;
  }

  public String getHotelLocation() {
    return hotelLocation;
  }

  public void setHotelLocation(String hotelLocation) {
    this.hotelLocation = hotelLocation;
  }

  public String getAmenities() {
    return amenities;
  }

  public void setAmenities(String amenities) {
    this.amenities = amenities;
  }

  public Integer getNights() {
    return nights;
  }

  public void setNights(Integer nights) {
    this.nights = nights;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
}
