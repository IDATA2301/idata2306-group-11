package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {

}
