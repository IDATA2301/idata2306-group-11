package com.roamroute.backend.dto;

/**
 * DTO representing a hotel pricing option for a trip.
 *
 * <p>Contains pricing (price, provider) and full hotel details (name, type, location,
 * amenities, stay duration, coordinates). Used to show hotel options to users.
 */
public class HotelOptionDTO {

  private int id;
  private String provider;
  private double price;
  private Integer accommodationId;
  private String hotelName;
  private String hotelType;
  private String hotelCity;
  private String hotelLocation;
  private String amenities;
  private int nights;
  private double latitude;
  private double longitude;

  public HotelOptionDTO(int id, String provider, double price, Integer accommodationId, String hotelName,
                        String hotelType, String hotelCity, String hotelLocation, String amenities,
                        int nights, double latitude, double longitude) {
    this.id = id;
    this.provider = provider;
    this.price = price;
    this.accommodationId = accommodationId;
    this.hotelName = hotelName;
    this.hotelType = hotelType;
    this.hotelCity = hotelCity;
    this.hotelLocation = hotelLocation;
    this.amenities = amenities;
    this.nights = nights;
    this.latitude = latitude;
    this.longitude = longitude;
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

  public Integer getAccommodationId() {
    return accommodationId;
  }

  public String getHotelName() {
    return hotelName;
  }

  public String getHotelType() {
    return hotelType;
  }

  public String getHotelCity() {
    return hotelCity;
  }

  public String getHotelLocation() {
    return hotelLocation;
  }

  public String getAmenities() {
    return amenities;
  }

  public int getNights() {
    return nights;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
