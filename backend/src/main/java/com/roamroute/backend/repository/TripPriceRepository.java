package com.roamroute.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.TripPrice;

/**
 * Spring Data JPA repository for TripPrice entity, providing custom query methods to find flight and accommodation pricing by trip.
 */
public interface TripPriceRepository extends JpaRepository<TripPrice, Integer> {
  List<TripPrice> findByTrip_IdAndFlightIsNotNull(int tripId);
  List<TripPrice> findByTrip_IdAndAccommodationIsNotNull(int tripId);

  Optional<TripPrice> findByTrip_idAndFlight_id(int tripId, int flightId);
  Optional<TripPrice> findByTrip_idAndAccommodation_id(int tripId, int accommodation);

  long countByFlight_Id(int flightId);
  long countByAccommodation_Id(int accommodationId);

  void deleteByTrip_Id(int tripId);
}
