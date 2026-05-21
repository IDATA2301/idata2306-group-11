package com.roamroute.backend.dto;

/**
 * Request for creating or updating a flight record.
 *
 * <p>Contains flight details such as airline, departure/destination cities and
 * airports, and duration. Used by admins to add or modify flights in the catalog.
 */
public class FlightRequest {

  private String airline;
  private String departureCity;
  private String destinationCity;
  private String departureAirport;
  private String destinationAirport;
  private String flightDuration;

  public String getAirline() {
    return airline;
  }

  public void setAirline(String airline) {
    this.airline = airline;
  }

  public String getDepartureCity() {
    return departureCity;
  }

  public void setDepartureCity(String departureCity) {
    this.departureCity = departureCity;
  }

  public String getDestinationCity() {
    return destinationCity;
  }

  public void setDestinationCity(String destinationCity) {
    this.destinationCity = destinationCity;
  }

  public String getDepartureAirport() {
    return departureAirport;
  }

  public void setDepartureAirport(String departureAirport) {
    this.departureAirport = departureAirport;
  }

  public String getDestinationAirport() {
    return destinationAirport;
  }

  public void setDestinationAirport(String destinationAirport) {
    this.destinationAirport = destinationAirport;
  }

  public String getFlightDuration() {
    return flightDuration;
  }

  public void setFlightDuration(String flightDuration) {
    this.flightDuration = flightDuration;
  }
}
