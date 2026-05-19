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

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("/api/trips")
@SecurityRequirements
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/search")
    public List<TripHomeDTO> searchTrips(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer destinationId
    ) {
        return tripService.searchTrips(q, minPrice, maxPrice, destinationId);
    }

    @GetMapping("/home")
    public List<TripHomeDTO> getHomeTrips() {
        return tripService.getHomeTrips();
    }

    @GetMapping("/{id}")
    public TripDetailsDTO getTripById(@PathVariable int id) {
        return tripService.getTripDetails(id);
    }
}
