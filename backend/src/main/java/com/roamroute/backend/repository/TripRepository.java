package com.roamroute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roamroute.backend.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

  List<Trip> findTop3ByOrderByIdAsc();

  Trip findById(int id);

  @Query("""
      SELECT DISTINCT t
      FROM Trip t
      JOIN t.destination d
      WHERE LOWER(COALESCE(t.title, '')) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(COALESCE(t.trip_description, '')) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(COALESCE(t.keywords, '')) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(COALESCE(d.city, '')) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(COALESCE(d.country, '')) LIKE LOWER(CONCAT('%', :query, '%'))
      ORDER BY t.start_date ASC
      """)
  List<Trip> searchTrips(@Param("query") String query);
}
