package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

}
