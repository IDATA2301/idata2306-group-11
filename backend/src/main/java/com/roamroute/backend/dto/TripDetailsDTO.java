package com.roamroute.backend.dto;

import java.util.List;

public class TripDetailsDTO {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private String city;
    private String country;
    private String startDate;
    private String endDate;
    private String flightDuration;
    private List<TripPriceDTO> flightOptions;
    private List<TripPriceDTO> hotelOptions;


    public TripDetailsDTO(int id, String title, String description,
                          String imageUrl, String city, String country,
                          String startDate, String endDate,
                          String flightDuration, List<TripPriceDTO> flightOptions, List<TripPriceDTO> hotelOptions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.city = city;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flightDuration = flightDuration;
        this.flightOptions = flightOptions;
        this.hotelOptions = hotelOptions;
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

}