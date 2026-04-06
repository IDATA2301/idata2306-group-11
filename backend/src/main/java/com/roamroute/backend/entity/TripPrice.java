package com.roamroute.backend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tripprices")
public class TripPrice {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "trip_id")
  @JsonIgnoreProperties({"tripPrices"})
  private Trip trip;

  private String tripprice_provider;

  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(name = "tripprice_type")
  private TripPriceType tripprice_type;

  @ManyToOne
  @JoinColumn(name = "flight_id")
  @JsonIgnoreProperties({"tripPrices"})
  private Flight flight;

  @ManyToOne
  @JoinColumn(name = "accommodation_id")
  @JsonIgnoreProperties({"tripPrices"})
  private Accommodation accommodation;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTripprice_provider() {
    return tripprice_provider;
  }

  public void setTripprice_provider(String tripprice_provider) {
    this.tripprice_provider = tripprice_provider;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public TripPriceType getTripprice_type() {
    return tripprice_type;
  }

  public void setTripprice_type(TripPriceType tripprice_type) {
    this.tripprice_type = tripprice_type;
  }

  public Trip getTrip() {
    return trip;
  }

  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public Accommodation getAccommodation() {
    return accommodation;
  }

  public void setAccommodation(Accommodation accommodation) {
    this.accommodation = accommodation;
  }
}
