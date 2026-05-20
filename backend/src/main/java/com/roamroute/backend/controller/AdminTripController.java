package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.dto.UpdateTripRequest;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/trips")
@Tag(name = "Admin / Trips", description = "Manage trips")
public class AdminTripController {

  private final TripService tripService;
	private final TripRepository tripRepository;

  public AdminTripController(TripService tripService, TripRepository tripRepository) {
    this.tripService = tripService;
		this.tripRepository = tripRepository;
  }

	@GetMapping
	public List<TripHomeDTO> getAllTripsForAdmin() {
		return tripService.getAllTripsForAdmin();
	}

	@PatchMapping("/{id}/toggle")
	public Trip toggleTrip(@PathVariable int id) {
		Trip trip = tripRepository.findById(id)
			.orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

		trip.setActive(!trip.isActive());
		return tripRepository.save(trip);
	}


    @PutMapping("/{id}")
    @Operation(summary = "Update an existing trip")
    public TripDetailsDTO updateTrip(@PathVariable int id, @RequestBody UpdateTripRequest request) {
        return tripService.updateTrip(id, request);
    }

}
