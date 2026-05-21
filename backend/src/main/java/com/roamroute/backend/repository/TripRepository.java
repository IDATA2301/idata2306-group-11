package com.roamroute.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roamroute.backend.entity.Trip;

/**
 * Spring Data JPA repository for Trip entity, providing custom query methods for searching active trips and random trip selection.
 */
public interface TripRepository extends JpaRepository<Trip, Integer> {

  @Query(value = "SELECT * FROM trips WHERE active = true ORDER BY RAND() LIMIT 3", nativeQuery = true)
  List<Trip> find3RandomActive();
  
  Optional<Trip> findById(int id);

  @Query("""
      SELECT DISTINCT t
      FROM Trip t
      JOIN t.destination d
      LEFT JOIN TripPrice tp ON tp.trip = t
      LEFT JOIN tp.flight f
      LEFT JOIN tp.accommodation a
      WHERE t.active = true
      AND (
        LOWER(COALESCE(t.title, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(t.trip_description, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(t.keywords, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(d.city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(d.country, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(f.airline, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(f.departure_city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(f.destination_city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(f.departure_airport, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(f.destination_airport, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(a.hotel_name, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(a.hotel_type, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(a.hotel_city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(a.hotel_location, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(COALESCE(a.amenities, '')) LIKE LOWER(CONCAT('%', :query, '%'))
      )
      ORDER BY t.start_date ASC
      """)
  List<Trip> searchActiveTrips(@Param("query") String query);

  List<Trip> findByActiveTrue();

  @Query("SELECT COALESCE(MAX(t.id), 0) FROM Trip t")
  int findMaxId();

}
