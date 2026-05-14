package com.roamroute.backend.dto;

public class FlightOptionDTO {

  private int id;
  private String provider;
  private double price;
  private Integer flightId;
  private String airline;
  private String departureCity;
  private String destinationCity;
  private String departureAirport;
  private String destinationAirport;
  private String flightDuration;

  public FlightOptionDTO(int id, String provider, double price, Integer flightId, String airline,
                         String departureCity, String destinationCity, String departureAirport,
                         String destinationAirport, String flightDuration) {
    this.id = id;
    this.provider = provider;
    this.price = price;
    this.flightId = flightId;
    this.airline = airline;
    this.departureCity = departureCity;
    this.destinationCity = destinationCity;
    this.departureAirport = departureAirport;
    this.destinationAirport = destinationAirport;
    this.flightDuration = flightDuration;
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

  public Integer getFlightId() {
    return flightId;
  }

  public String getAirline() {
    return airline;
  }

  public String getDepartureCity() {
    return departureCity;
  }

  public String getDestinationCity() {
    return destinationCity;
  }

  public String getDepartureAirport() {
    return departureAirport;
  }

  public String getDestinationAirport() {
    return destinationAirport;
  }

  public String getFlightDuration() {
    return flightDuration;
  }
}
