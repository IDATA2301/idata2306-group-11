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

import com.roamroute.backend.dto.AccommodationDTO;
import com.roamroute.backend.dto.AccommodationRequest;
import com.roamroute.backend.service.AccommodationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Administrative REST controller for managing accommodations.
 *
 * <p>Provides endpoints for listing, retrieving, creating, updating and deleting
 * accommodations used by the admin UI. All methods delegate to the
 * {@link com.roamroute.backend.service.AccommodationService}.
 */
@RestController
@RequestMapping("/api/admin/accommodations")
@Tag(name = "Admin / Accommodations", description = "Manage accommodations catalog")
public class AdminAccommodationController {

  private final AccommodationService accommodationService;

  public AdminAccommodationController(AccommodationService accommodationService) {
    this.accommodationService = accommodationService;
  }

  @GetMapping
  @Operation(summary = "List all accommodations")
  public List<AccommodationDTO> list() {
    return accommodationService.list();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a single accommodation by ID")
  public AccommodationDTO get(@PathVariable int id) {
    return accommodationService.get(id);
  }

  @PostMapping
  @Operation(summary = "Create a new accommodation")
  public AccommodationDTO create(@RequestBody AccommodationRequest request) {
    return accommodationService.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing accommodation")
  public AccommodationDTO update(@PathVariable int id, @RequestBody AccommodationRequest request) {
    return accommodationService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an accommodation")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    accommodationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
