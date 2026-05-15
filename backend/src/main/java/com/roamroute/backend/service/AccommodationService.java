package com.roamroute.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.AccommodationDTO;
import com.roamroute.backend.dto.AccommodationRequest;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.repository.AccommodationRepository;
import com.roamroute.backend.repository.TripPriceRepository;

@Service
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;
  private final TripPriceRepository tripPriceRepository;

  public AccommodationService(AccommodationRepository accommodationRepository,
                              TripPriceRepository tripPriceRepository) {
    this.accommodationRepository = accommodationRepository;
    this.tripPriceRepository = tripPriceRepository;
  }

  public List<AccommodationDTO> list() {
    return accommodationRepository.findAll().stream().map(AccommodationDTO::from).toList();
  }

  public AccommodationDTO get(int id) {
    return AccommodationDTO.from(require(id));
  }

  @Transactional
  public AccommodationDTO create(AccommodationRequest request) {
    Accommodation accommodation = new Accommodation();
    apply(accommodation, request);
    return AccommodationDTO.from(accommodationRepository.save(accommodation));
  }

  @Transactional
  public AccommodationDTO update(int id, AccommodationRequest request) {
    Accommodation accommodation = require(id);
    apply(accommodation, request);
    return AccommodationDTO.from(accommodationRepository.save(accommodation));
  }

  @Transactional
  public void delete(int id) {
    Accommodation accommodation = require(id);
    if (tripPriceRepository.countByAccommodation_Id(id) > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot delete accommodation: it is still used by one or more trip options.");
    }
    accommodationRepository.delete(accommodation);
  }

  private Accommodation require(int id) {
    return accommodationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accommodation not found"));
  }

  private void apply(Accommodation accommodation, AccommodationRequest request) {
    if (request.getHotelName() != null) accommodation.setHotel_name(request.getHotelName());
    if (request.getHotelType() != null) accommodation.setHotel_type(request.getHotelType());
    if (request.getHotelCity() != null) accommodation.setHotel_city(request.getHotelCity());
    if (request.getHotelLocation() != null) accommodation.setHotel_location(request.getHotelLocation());
    if (request.getAmenities() != null) accommodation.setAmenities(request.getAmenities());
    if (request.getNights() != null) accommodation.setNights(request.getNights());
    if (request.getLatitude() != null) accommodation.setLatitude(request.getLatitude());
    if (request.getLongitude() != null) accommodation.setLongitude(request.getLongitude());
  }
}
