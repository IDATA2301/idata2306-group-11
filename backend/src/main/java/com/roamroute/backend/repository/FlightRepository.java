package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer> {

}
