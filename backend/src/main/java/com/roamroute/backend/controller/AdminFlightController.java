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

import com.roamroute.backend.dto.FlightDTO;
import com.roamroute.backend.dto.FlightRequest;
import com.roamroute.backend.service.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Admin controller for managing flight records.
 *
 * <p>Provides endpoints to list, retrieve, create, update and delete flight
 * entries used by the admin UI. Operations delegate to
 * {@link com.roamroute.backend.service.FlightService}.
 */
@RestController
@RequestMapping("/api/admin/flights")
@Tag(name = "Admin / Flights", description = "Manage flights catalog")
public class AdminFlightController {

  private final FlightService flightService;

  public AdminFlightController(FlightService flightService) {
    this.flightService = flightService;
  }

  @GetMapping
  @Operation(summary = "List all flights")
  public List<FlightDTO> list() {
    return flightService.list();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a single flight by ID")
  public FlightDTO get(@PathVariable int id) {
    return flightService.get(id);
  }

  @PostMapping
  @Operation(summary = "Create a new flight")
  public FlightDTO create(@RequestBody FlightRequest request) {
    return flightService.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing flight")
  public FlightDTO update(@PathVariable int id, @RequestBody FlightRequest request) {
    return flightService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a flight")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    flightService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
