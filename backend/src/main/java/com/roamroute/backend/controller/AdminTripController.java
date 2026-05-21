package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.CreateTripRequest;
import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.dto.UpdateTripRequest;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Administrative controller for managing trips.
 *
 * <p>Exposes CRUD endpoints for trips and a small toggle endpoint to activate/deactivate
 * a trip. The controller delegates business logic to {@link com.roamroute.backend.service.TripService}.
 */
@RestController
@RequestMapping("/api/admin/trips")
@Tag(name = "Admin / Trips", description = "Manage trips catalog")
public class AdminTripController {

  private final TripService tripService;
  private final TripRepository tripRepository;

  public AdminTripController(TripService tripService, TripRepository tripRepository) {
    this.tripService = tripService;
    this.tripRepository = tripRepository;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a new trip")
  public TripDetailsDTO createTrip(@RequestBody CreateTripRequest request) {
    return tripService.createTrip(request);
  }


	@GetMapping
	@Operation(summary = "List all trips (admin, including inactive)")
	public List<TripHomeDTO> getAllTripsForAdmin() {
		return tripService.getAllTripsForAdmin();
	}
  
  @GetMapping("/{id}")
  @Operation(summary = "Get a single trip by ID (admin, including inactive)")
  public TripDetailsDTO getTrip(@PathVariable int id) {
    return tripService.getTripDetailsForAdmin(id);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing trip")
  public TripDetailsDTO updateTrip(@PathVariable int id, @RequestBody UpdateTripRequest request) {
    return tripService.updateTrip(id, request);
  }


  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a trip")
  public ResponseEntity<Void> deleteTrip(@PathVariable int id) {
    tripService.deleteTrip(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/toggle")
  @Operation(summary = "Toggle a trip's active/inactive status")
  public Trip toggleTrip(@PathVariable int id) {
    Trip trip = tripRepository.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

    trip.setActive(!trip.isActive());
    return tripRepository.save(trip);
  }

}
