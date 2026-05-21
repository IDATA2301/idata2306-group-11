package com.roamroute.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.FlightDTO;
import com.roamroute.backend.dto.FlightRequest;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.repository.FlightRepository;
import com.roamroute.backend.repository.TripPriceRepository;

@Service
/**
 * Service for managing flight entities with CRUD operations and validation to prevent deletion of referenced flights.
 */
public class FlightService {

  private final FlightRepository flightRepository;
  private final TripPriceRepository tripPriceRepository;

  public FlightService(FlightRepository flightRepository, TripPriceRepository tripPriceRepository) {
    this.flightRepository = flightRepository;
    this.tripPriceRepository = tripPriceRepository;
  }

  public List<FlightDTO> list() {
    return flightRepository.findAll().stream().map(FlightDTO::from).toList();
  }

  public FlightDTO get(int id) {
    return FlightDTO.from(require(id));
  }

  @Transactional
  public FlightDTO create(FlightRequest request) {
    Flight flight = new Flight();
    apply(flight, request);
    return FlightDTO.from(flightRepository.save(flight));
  }

  @Transactional
  public FlightDTO update(int id, FlightRequest request) {
    Flight flight = require(id);
    apply(flight, request);
    return FlightDTO.from(flightRepository.save(flight));
  }

  @Transactional
  public void delete(int id) {
    Flight flight = require(id);
    if (tripPriceRepository.countByFlight_Id(id) > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot delete flight: it is still used by one or more trip options.");
    }
    flightRepository.delete(flight);
  }

  private Flight require(int id) {
    return flightRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
  }

  private void apply(Flight flight, FlightRequest request) {
    if (request.getAirline() != null) flight.setAirline(request.getAirline());
    if (request.getDepartureCity() != null) flight.setDeparture_city(request.getDepartureCity());
    if (request.getDestinationCity() != null) flight.setDestination_city(request.getDestinationCity());
    if (request.getDepartureAirport() != null) flight.setDeparture_airport(request.getDepartureAirport());
    if (request.getDestinationAirport() != null) flight.setDestination_airport(request.getDestinationAirport());
    if (request.getFlightDuration() != null) flight.setFlight_duration(request.getFlightDuration());
  }
}
