package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Integer> {

}
