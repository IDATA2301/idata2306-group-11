package com.roamroute.backend.dto;

import com.roamroute.backend.entity.Flight;

public class FlightDTO {

  private int id;
  private String airline;
  private String departureCity;
  private String destinationCity;
  private String departureAirport;
  private String destinationAirport;
  private String flightDuration;

  public FlightDTO(int id, String airline, String departureCity, String destinationCity,
                   String departureAirport, String destinationAirport, String flightDuration) {
    this.id = id;
    this.airline = airline;
    this.departureCity = departureCity;
    this.destinationCity = destinationCity;
    this.departureAirport = departureAirport;
    this.destinationAirport = destinationAirport;
    this.flightDuration = flightDuration;
  }

  public static FlightDTO from(Flight flight) {
    return new FlightDTO(
        flight.getId(),
        flight.getAirline(),
        flight.getDeparture_city(),
        flight.getDestination_city(),
        flight.getDeparture_airport(),
        flight.getDestination_airport(),
        flight.getFlight_duration());
  }

  public int getId() {
    return id;
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
