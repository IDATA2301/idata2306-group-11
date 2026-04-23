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

}
