package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.repository.TripRepository;

@RestController
@RequestMapping("/api/trips")
public class TripController {
  
	private final TripRepository tripRepository;

	public TripController(TripRepository tripRepository) {
		this.tripRepository = tripRepository;
	}

	@GetMapping
	public List<Trip> getAllTrips() {
		return tripRepository.findAll();
	}

	@GetMapping("/{id}")
	public Trip getTripById(@PathVariable int id) {
		return tripRepository.findById(id).orElseThrow();
	}

	@GetMapping("/home")
	public List<Trip> getHomeTrips() {
		return tripRepository.findTop3ByOrderByIdAsc();
	}
}
