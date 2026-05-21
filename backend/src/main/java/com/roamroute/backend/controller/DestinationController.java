package com.roamroute.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



/**
 * Public controller exposing destination data to clients.
 *
 * <p>Provides a simple read-only endpoint to list available destinations used
 * by the public-facing UI. Data is fetched from the {@link com.roamroute.backend.repository.DestinationRepository}.
 */
@RestController
@RequestMapping("/api/destinations")
@SecurityRequirements
@Tag(name = "Destinations", description = "Public list of destinations")
public class DestinationController {

  private final DestinationRepository destinationRepository;

  public DestinationController(DestinationRepository destinationRepository) {
		this.destinationRepository = destinationRepository;
  }

  @GetMapping
  @Operation(summary = "List all destinations available to public users")
  public List<Destination> getAllDestinations() {
    return destinationRepository.findAll();
  }
    
}
