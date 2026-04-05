package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.service.TripService;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
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