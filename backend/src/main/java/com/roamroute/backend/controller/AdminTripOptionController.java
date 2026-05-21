package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.FlightOptionDTO;
import com.roamroute.backend.dto.FlightOptionRequest;
import com.roamroute.backend.dto.HotelOptionDTO;
import com.roamroute.backend.dto.HotelOptionRequest;
import com.roamroute.backend.service.TripOptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Administrative Controller for managing per-trip pricing options (flights and hotels).
 *
 * <p>Allows admins to list, create, update and delete flight and hotel pricing
 * options for a given trip. All operations delegate to
 * {@link com.roamroute.backend.service.TripOptionService}.
 */
@RestController
@RequestMapping("/api/admin/trips/{tripId}")
@Tag(name = "Admin / Trip Options", description = "Manage flight and hotel pricing options per trip")
public class AdminTripOptionController {

  private final TripOptionService tripOptionService;

  public AdminTripOptionController(TripOptionService tripOptionService) {
    this.tripOptionService = tripOptionService;
  }

  @GetMapping("/flight-options")
  @Operation(summary = "List flight pricing options for a trip")
  public List<FlightOptionDTO> listFlightOptions(@PathVariable int tripId) {
    return tripOptionService.listFlightOptions(tripId);
  }

  @PostMapping("/flight-options")
  @Operation(summary = "Add a flight pricing option to a trip")
  public FlightOptionDTO createFlightOption(@PathVariable int tripId,
                                            @RequestBody FlightOptionRequest request) {
    return tripOptionService.createFlightOption(tripId, request);
  }

  @PutMapping("/flight-options/{tripPriceId}")
  @Operation(summary = "Update a flight pricing option")
  public FlightOptionDTO updateFlightOption(@PathVariable int tripId,
                                            @PathVariable int tripPriceId,
                                            @RequestBody FlightOptionRequest request) {
    return tripOptionService.updateFlightOption(tripId, tripPriceId, request);
  }

  @DeleteMapping("/flight-options/{tripPriceId}")
  @Operation(summary = "Delete a flight pricing option")
  public ResponseEntity<Void> deleteFlightOption(@PathVariable int tripId,
                                                 @PathVariable int tripPriceId) {
    tripOptionService.deleteFlightOption(tripId, tripPriceId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/hotel-options")
  @Operation(summary = "List hotel pricing options for a trip")
  public List<HotelOptionDTO> listHotelOptions(@PathVariable int tripId) {
    return tripOptionService.listHotelOptions(tripId);
  }

  @PostMapping("/hotel-options")
  @Operation(summary = "Add a hotel pricing option to a trip")
  public HotelOptionDTO createHotelOption(@PathVariable int tripId,
                                          @RequestBody HotelOptionRequest request) {
    return tripOptionService.createHotelOption(tripId, request);
  }

  @PutMapping("/hotel-options/{tripPriceId}")
  @Operation(summary = "Update a hotel pricing option")
  public HotelOptionDTO updateHotelOption(@PathVariable int tripId,
                                          @PathVariable int tripPriceId,
                                          @RequestBody HotelOptionRequest request) {
    return tripOptionService.updateHotelOption(tripId, tripPriceId, request);
  }

  @DeleteMapping("/hotel-options/{tripPriceId}")
  @Operation(summary = "Delete a hotel pricing option")
  public ResponseEntity<Void> deleteHotelOption(@PathVariable int tripId,
                                                @PathVariable int tripPriceId) {
    tripOptionService.deleteHotelOption(tripId, tripPriceId);
    return ResponseEntity.noContent().build();
  }
}
