package com.roamroute.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.DestinationDTO;
import com.roamroute.backend.dto.UpdateDestinationRequest;
import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.repository.DestinationRepository;

@RestController
@RequestMapping("/api/admin/destinations")
public class AdminDestinationController {

  private final DestinationRepository destinationRepository;

  public AdminDestinationController(DestinationRepository destinationRepository) {
    this.destinationRepository = destinationRepository;
  }

  @PutMapping("/{id}")
  public DestinationDTO updateDestination(@PathVariable int id, @RequestBody UpdateDestinationRequest request) {
    Destination destination = destinationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination not found"));

    if (request.getCity() != null) {
      destination.setCity(request.getCity());
    }
    if (request.getCountry() != null) {
      destination.setCountry(request.getCountry());
    }
    if (request.getImageUrl() != null) {
      destination.setImage_url(request.getImageUrl());
    }
    if (request.getImageAlt() != null) {
      destination.setImage_alt(request.getImageAlt());
    }

    Destination saved = destinationRepository.save(destination);
    return new DestinationDTO(
        saved.getId(),
        saved.getCity(),
        saved.getCountry(),
        saved.getImage_url(),
        saved.getImage_alt());
  }
}
