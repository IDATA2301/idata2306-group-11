package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.DestinationDTO;
import com.roamroute.backend.dto.UpdateDestinationRequest;
import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.repository.DestinationRepository;
import com.roamroute.backend.repository.TripRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/destinations")
@Tag(name = "Admin / Destinations", description = "Manage destinations catalog")
public class AdminDestinationController {

  private final DestinationRepository destinationRepository;
  private final TripRepository tripRepository;

  public AdminDestinationController(DestinationRepository destinationRepository,
                                    TripRepository tripRepository) {
    this.destinationRepository = destinationRepository;
    this.tripRepository = tripRepository;
  }

  @GetMapping
  @Operation(summary = "List all destinations (admin)")
  public List<DestinationDTO> list() {
    return destinationRepository.findAll().stream().map(this::toDTO).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a single destination by ID (admin)")
  public DestinationDTO get(@PathVariable int id) {
    return toDTO(require(id));
  }

  @PostMapping
  @Operation(summary = "Create a new destination")
  public DestinationDTO create(@RequestBody UpdateDestinationRequest request) {
    Destination destination = new Destination();
    apply(destination, request);
    return toDTO(destinationRepository.save(destination));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing destination")
  public DestinationDTO update(@PathVariable int id, @RequestBody UpdateDestinationRequest request) {
    Destination destination = require(id);
    apply(destination, request);
    return toDTO(destinationRepository.save(destination));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a destination (rejected if used by any trip)")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    Destination destination = require(id);
    boolean inUse = tripRepository.findAll().stream()
        .anyMatch(trip -> trip.getDestination() != null && trip.getDestination().getId() == id);
    if (inUse) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot delete destination: it is still used by one or more trips.");
    }
    destinationRepository.delete(destination);
    return ResponseEntity.noContent().build();
  }

  private Destination require(int id) {
    return destinationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination not found"));
  }

  private void apply(Destination destination, UpdateDestinationRequest request) {
    if (request.getCity() != null) destination.setCity(request.getCity());
    if (request.getCountry() != null) destination.setCountry(request.getCountry());
    if (request.getImageUrl() != null) destination.setImage_url(request.getImageUrl());
    if (request.getImageAlt() != null) destination.setImage_alt(request.getImageAlt());
  }

  private DestinationDTO toDTO(Destination destination) {
    return new DestinationDTO(
        destination.getId(),
        destination.getCity(),
        destination.getCountry(),
        destination.getImage_url(),
        destination.getImage_alt());
  }
}
