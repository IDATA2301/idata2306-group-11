package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Destination;

/**
 * Spring Data JPA repository for Destination entity.
 */
public interface DestinationRepository extends JpaRepository<Destination, Integer> {

}
