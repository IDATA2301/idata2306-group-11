package com.roamroute.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "selectedpackages")
/**
 * JPA entity representing a selected package containing a trip with chosen flight and hotel pricing options.
 */
public class SelectedPackage {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  @ManyToOne
  @JoinColumn(name = "flight_tripprice_id", nullable = false)
  private TripPrice flightTripPrice;

  @ManyToOne
  @JoinColumn(name = "hotel_tripprice_id", nullable = false)
  private TripPrice hotelTripPrice;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Trip getTrip() {
    return trip;
  }

  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  public TripPrice getFlightTripPrice() {
    return flightTripPrice;
  }

  public void setFlightTripPrice(TripPrice flightTripPrice) {
    this.flightTripPrice = flightTripPrice;
  }

  public TripPrice getHotelTripPrice() {
    return hotelTripPrice;
  }

  public void setHotelTripPrice(TripPrice hotelTripPrice) {
    this.hotelTripPrice = hotelTripPrice;
  }


}
