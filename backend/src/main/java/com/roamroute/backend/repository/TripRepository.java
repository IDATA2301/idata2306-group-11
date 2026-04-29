package com.roamroute.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roamroute.backend.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

  List<Trip> findTop3ByOrderByIdAsc();
  
  Optional<Trip> findById(int id);

  @Query("""
      SELECT DISTINCT t
      FROM Trip t
      JOIN t.destination d
      LEFT JOIN TripPrice tp ON tp.trip = t
      LEFT JOIN tp.flight f
      LEFT JOIN tp.accommodation a
      WHERE LOWER(COALESCE(t.title, '')) LIKE LOWER(CONCAT('%', :query, '%'))
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
      ORDER BY t.start_date ASC
      """)
  List<Trip> searchTrips(@Param("query") String query);

}
