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

@RestController
@RequestMapping("/api/admin/trips/{tripId}")
public class AdminTripOptionController {

  private final TripOptionService tripOptionService;

  public AdminTripOptionController(TripOptionService tripOptionService) {
    this.tripOptionService = tripOptionService;
  }

  @GetMapping("/flight-options")
  public List<FlightOptionDTO> listFlightOptions(@PathVariable int tripId) {
    return tripOptionService.listFlightOptions(tripId);
  }

  @PostMapping("/flight-options")
  public FlightOptionDTO createFlightOption(@PathVariable int tripId,
                                            @RequestBody FlightOptionRequest request) {
    return tripOptionService.createFlightOption(tripId, request);
  }

  @PutMapping("/flight-options/{tripPriceId}")
  public FlightOptionDTO updateFlightOption(@PathVariable int tripId,
                                            @PathVariable int tripPriceId,
                                            @RequestBody FlightOptionRequest request) {
    return tripOptionService.updateFlightOption(tripId, tripPriceId, request);
  }

  @DeleteMapping("/flight-options/{tripPriceId}")
  public ResponseEntity<Void> deleteFlightOption(@PathVariable int tripId,
                                                 @PathVariable int tripPriceId) {
    tripOptionService.deleteFlightOption(tripId, tripPriceId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/hotel-options")
  public List<HotelOptionDTO> listHotelOptions(@PathVariable int tripId) {
    return tripOptionService.listHotelOptions(tripId);
  }

  @PostMapping("/hotel-options")
  public HotelOptionDTO createHotelOption(@PathVariable int tripId,
                                          @RequestBody HotelOptionRequest request) {
    return tripOptionService.createHotelOption(tripId, request);
  }

  @PutMapping("/hotel-options/{tripPriceId}")
  public HotelOptionDTO updateHotelOption(@PathVariable int tripId,
                                          @PathVariable int tripPriceId,
                                          @RequestBody HotelOptionRequest request) {
    return tripOptionService.updateHotelOption(tripId, tripPriceId, request);
  }

  @DeleteMapping("/hotel-options/{tripPriceId}")
  public ResponseEntity<Void> deleteHotelOption(@PathVariable int tripId,
                                                @PathVariable int tripPriceId) {
    tripOptionService.deleteHotelOption(tripId, tripPriceId);
    return ResponseEntity.noContent().build();
  }
}
