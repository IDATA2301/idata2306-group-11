package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.TripPrice;

public interface TripPriceRepository extends JpaRepository<TripPrice, Integer> {

}
