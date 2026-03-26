package com.roamroute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.TripPrice;

public interface TripPriceRepository extends JpaRepository<TripPrice, Integer> {
  List<TripPrice> findByTrip_IdAndFlightIsNotNull(int tripId);
  List<TripPrice> findByTrip_IdAndAccommodationIsNotNull(int tripId);
}
