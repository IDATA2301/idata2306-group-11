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
  int id;

  String airline;
  String departure_city;
  String arrival_city;
  String departure_airport;
  String arrival_airport;
  String flight_duration;

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

  public String getArrival_city() {
    return arrival_city;
  }

  public void setArrival_city(String arrival_city) {
    this.arrival_city = arrival_city;
  }

  public String getDeparture_airport() {
    return departure_airport;
  }

  public void setDeparture_airport(String departure_airport) {
    this.departure_airport = departure_airport;
  }

  public String getArrival_airport() {
    return arrival_airport;
  }

  public void setArrival_airport(String arrival_airport) {
    this.arrival_airport = arrival_airport;
  }

  public String getFlight_duration() {
    return flight_duration;
  }

  public void setFlight_duration(String flight_duration) {
    this.flight_duration = flight_duration;
  }


}
