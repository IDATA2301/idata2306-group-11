package com.roamroute.backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.UpdateTripRequest;
import com.roamroute.backend.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/trips")
@Tag(name = "Admin / Trips", description = "Manage trips")
public class AdminTripController {

    private final TripService tripService;

    public AdminTripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing trip")
    public TripDetailsDTO updateTrip(@PathVariable int id, @RequestBody UpdateTripRequest request) {
        return tripService.updateTrip(id, request);
    }
}
