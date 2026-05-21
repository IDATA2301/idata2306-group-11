package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Accommodation;

/**
 * Spring Data JPA repository for Accommodation entity.
 */
public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {

}
