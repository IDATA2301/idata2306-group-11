package com.roamroute.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "flights")
public class Flight {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  private String airline;
  private String departure_city;
  private String destination_city;
  private String departure_airport;
  private String destination_airport;
  private String flight_duration;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAirline() {
    return airline;
  }

  public void setAirline(String airline) {
    this.airline = airline;
  }

  public String getDeparture_city() {
    return departure_city;
  }

  public void setDeparture_city(String departure_city) {
    this.departure_city = departure_city;
  }

  public String getDestination_city() {
    return destination_city;
  }

  public void setDestination_city(String arrival_city) {
    this.destination_city = arrival_city;
  }

  public String getDeparture_airport() {
    return departure_airport;
  }

  public void setDeparture_airport(String departure_airport) {
    this.departure_airport = departure_airport;
  }

  public String getDestination_airport() {
    return destination_airport;
  }

  public void setDestination_airport(String destination_airport) {
    this.destination_airport = destination_airport;
  }

  public String getFlight_duration() {
    return flight_duration;
  }

  public void setFlight_duration(String flight_duration) {
    this.flight_duration = flight_duration;
  }


}
