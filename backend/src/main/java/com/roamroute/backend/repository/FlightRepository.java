package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Flight;

/**
 * Spring Data JPA repository for Flight entity.
 */
public interface FlightRepository extends JpaRepository<Flight, Integer> {

}
