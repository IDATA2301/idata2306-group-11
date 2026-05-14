package com.roamroute.backend.dto;

import java.util.List;

public class TripDetailsDTO {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer destinationId;
    private String city;
    private String country;
    private String destinationImageUrl;
    private String destinationImageAlt;
    private String startDate;
    private String endDate;
    private String flightDuration;
    private List<TripPriceDTO> flightOptions;
    private List<TripPriceDTO> hotelOptions;
    private String departureAirport;
    private String arrivalAirport;
    private List<String> keywords;
    private String hotelName;
    private String hotelType;
    private String hotelLocation;
    private String amenities;
    private int nights;
    private double latitude;
    private double longitude;

    public TripDetailsDTO(int id, String title, String description,
                          String imageUrl, Integer destinationId, String city, String country,
                          String destinationImageUrl, String destinationImageAlt,
                          String startDate, String endDate,
                          String flightDuration, List<TripPriceDTO> flightOptions, List<TripPriceDTO> hotelOptions,
                          String departureAirport, String arrivalAirport, List<String> keywords,
                          String hotelName, String hotelType, String hotelLocation, String amenities, int nights,
                          double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.destinationId = destinationId;
        this.city = city;
        this.country = country;
        this.destinationImageUrl = destinationImageUrl;
        this.destinationImageAlt = destinationImageAlt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flightDuration = flightDuration;
        this.flightOptions = flightOptions;
        this.hotelOptions = hotelOptions;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.keywords = keywords;
        this.hotelName = hotelName;
        this.hotelType = hotelType;
        this.hotelLocation = hotelLocation;
        this.amenities = amenities;
        this.nights = nights;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public String getDestinationImageUrl() {
        return destinationImageUrl;
    }

    public String getDestinationImageAlt() {
        return destinationImageAlt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public List<TripPriceDTO> getFlightOptions() {
        return flightOptions;
    }

    public List<TripPriceDTO> getHotelOptions() {
        return hotelOptions;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelType() {
        return hotelType;
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