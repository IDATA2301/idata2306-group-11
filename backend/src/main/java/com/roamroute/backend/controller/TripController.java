package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Public-facing controller for browsing and searching trips.
 *
 * <p>Exposes endpoints for searching across trips, listing featured home page
 * trips and fetching full trip details. All heavy-lifting is delegated to
 * {@link com.roamroute.backend.service.TripService}.
 */
@RestController
@RequestMapping("/api/trips")
@SecurityRequirements
@Tag(name = "Trips", description = "Public trip browsing and search")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search trips by text, price range, and destination")
    public List<TripHomeDTO> searchTrips(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer destinationId
    ) {
        return tripService.searchTrips(q, minPrice, maxPrice, destinationId);
    }

    @GetMapping("/home")
    @Operation(summary = "List trips featured on the home page")
    public List<TripHomeDTO> getHomeTrips() {
        return tripService.getHomeTrips();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full details of a single trip")
    public TripDetailsDTO getTripById(@PathVariable int id) {
        return tripService.getTripDetails(id);
    }
}
