package com.roamroute.backend.dto;

import com.roamroute.backend.entity.Accommodation;

/**
 * Data transfer object for accommodation (hotel) data.
 *
 * <p>Contains hotel details including name, type, location, amenities, stay duration
 * and coordinates. Used by the API to expose accommodation information in a clean,
 * controlled shape.
 */
public class AccommodationDTO {

  private int id;
  private String hotelName;
  private String hotelType;
  private String hotelCity;
  private String hotelLocation;
  private String amenities;
  private int nights;
  private double latitude;
  private double longitude;

  public AccommodationDTO(int id, String hotelName, String hotelType, String hotelCity,
                          String hotelLocation, String amenities, int nights,
                          double latitude, double longitude) {
    this.id = id;
    this.hotelName = hotelName;
    this.hotelType = hotelType;
    this.hotelCity = hotelCity;
    this.hotelLocation = hotelLocation;
    this.amenities = amenities;
    this.nights = nights;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static AccommodationDTO from(Accommodation accommodation) {
    return new AccommodationDTO(
        accommodation.getId(),
        accommodation.getHotel_name(),
        accommodation.getHotel_type(),
        accommodation.getHotel_city(),
        accommodation.getHotel_location(),
        accommodation.getAmenities(),
        accommodation.getNights(),
        accommodation.getLatitude(),
        accommodation.getLongitude());
  }

  public int getId() {
    return id;
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
