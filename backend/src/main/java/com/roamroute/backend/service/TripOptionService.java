package com.roamroute.backend.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.FlightOptionDTO;
import com.roamroute.backend.dto.FlightOptionRequest;
import com.roamroute.backend.dto.HotelOptionDTO;
import com.roamroute.backend.dto.HotelOptionRequest;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.TripPrice;
import com.roamroute.backend.entity.TripPriceType;
import com.roamroute.backend.repository.AccommodationRepository;
import com.roamroute.backend.repository.FlightRepository;
import com.roamroute.backend.repository.SelectedPackageRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;

@Service
public class TripOptionService {

  private final TripRepository tripRepository;
  private final TripPriceRepository tripPriceRepository;
  private final FlightRepository flightRepository;
  private final AccommodationRepository accommodationRepository;
  private final SelectedPackageRepository selectedPackageRepository;

  public TripOptionService(TripRepository tripRepository,
                           TripPriceRepository tripPriceRepository,
                           FlightRepository flightRepository,
                           AccommodationRepository accommodationRepository,
                           SelectedPackageRepository selectedPackageRepository) {
    this.tripRepository = tripRepository;
    this.tripPriceRepository = tripPriceRepository;
    this.flightRepository = flightRepository;
    this.accommodationRepository = accommodationRepository;
    this.selectedPackageRepository = selectedPackageRepository;
  }

  public List<FlightOptionDTO> listFlightOptions(int tripId) {
    requireTrip(tripId);
    return tripPriceRepository.findByTrip_IdAndFlightIsNotNull(tripId).stream()
        .map(this::toFlightDto)
        .sorted(Comparator.comparingDouble(FlightOptionDTO::getPrice))
        .toList();
  }

  public List<HotelOptionDTO> listHotelOptions(int tripId) {
    requireTrip(tripId);
    return tripPriceRepository.findByTrip_IdAndAccommodationIsNotNull(tripId).stream()
        .map(this::toHotelDto)
        .sorted(Comparator.comparingDouble(HotelOptionDTO::getPrice))
        .toList();
  }

  @Transactional
  public FlightOptionDTO createFlightOption(int tripId, FlightOptionRequest request) {
    Trip trip = requireTrip(tripId);
    if (request.getFlightId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "flightId is required");
    }
    Flight flight = flightRepository.findById(request.getFlightId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found"));

    TripPrice tripPrice = new TripPrice();
    tripPrice.setTrip(trip);
    tripPrice.setFlight(flight);
    tripPrice.setTripprice_type(TripPriceType.FLIGHT);
    tripPrice.setTripprice_provider(request.getProvider());
    tripPrice.setPrice(request.getPrice() == null ? BigDecimal.ZERO : request.getPrice());
    return toFlightDto(tripPriceRepository.save(tripPrice));
  }

  @Transactional
  public FlightOptionDTO updateFlightOption(int tripId, int tripPriceId, FlightOptionRequest request) {
    TripPrice tripPrice = requireFlightOption(tripId, tripPriceId);

    if (request.getFlightId() != null && (tripPrice.getFlight() == null
        || tripPrice.getFlight().getId() != request.getFlightId())) {
      Flight flight = flightRepository.findById(request.getFlightId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found"));
      tripPrice.setFlight(flight);
    }
    if (request.getProvider() != null) {
      tripPrice.setTripprice_provider(request.getProvider());
    }
    if (request.getPrice() != null) {
      tripPrice.setPrice(request.getPrice());
    }
    return toFlightDto(tripPriceRepository.save(tripPrice));
  }

  @Transactional
  public void deleteFlightOption(int tripId, int tripPriceId) {
    TripPrice tripPrice = requireFlightOption(tripId, tripPriceId);
    if (selectedPackageRepository.countByFlightTripPrice_Id(tripPriceId) > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot detach flight option referenced by an order or cart.");
    }
    tripPriceRepository.delete(tripPrice);
  }

  @Transactional
  public HotelOptionDTO createHotelOption(int tripId, HotelOptionRequest request) {
    Trip trip = requireTrip(tripId);
    if (request.getAccommodationId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accommodationId is required");
    }
    Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accommodation not found"));

    TripPrice tripPrice = new TripPrice();
    tripPrice.setTrip(trip);
    tripPrice.setAccommodation(accommodation);
    tripPrice.setTripprice_type(TripPriceType.HOTEL);
    tripPrice.setTripprice_provider(request.getProvider());
    tripPrice.setPrice(request.getPrice() == null ? BigDecimal.ZERO : request.getPrice());
    return toHotelDto(tripPriceRepository.save(tripPrice));
  }

  @Transactional
  public HotelOptionDTO updateHotelOption(int tripId, int tripPriceId, HotelOptionRequest request) {
    TripPrice tripPrice = requireHotelOption(tripId, tripPriceId);

    if (request.getAccommodationId() != null && (tripPrice.getAccommodation() == null
        || tripPrice.getAccommodation().getId() != request.getAccommodationId())) {
      Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accommodation not found"));
      tripPrice.setAccommodation(accommodation);
    }
    if (request.getProvider() != null) {
      tripPrice.setTripprice_provider(request.getProvider());
    }
    if (request.getPrice() != null) {
      tripPrice.setPrice(request.getPrice());
    }
    return toHotelDto(tripPriceRepository.save(tripPrice));
  }

  @Transactional
  public void deleteHotelOption(int tripId, int tripPriceId) {
    TripPrice tripPrice = requireHotelOption(tripId, tripPriceId);
    if (selectedPackageRepository.countByHotelTripPrice_Id(tripPriceId) > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot detach hotel option referenced by an order or cart.");
    }
    tripPriceRepository.delete(tripPrice);
  }

  private Trip requireTrip(int tripId) {
    return tripRepository.findById(tripId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
  }

  private TripPrice requireFlightOption(int tripId, int tripPriceId) {
    TripPrice tripPrice = tripPriceRepository.findById(tripPriceId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight option not found"));
    if (tripPrice.getTrip() == null || tripPrice.getTrip().getId() != tripId || tripPrice.getFlight() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight option not found for this trip");
    }
    return tripPrice;
  }

  private TripPrice requireHotelOption(int tripId, int tripPriceId) {
    TripPrice tripPrice = tripPriceRepository.findById(tripPriceId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel option not found"));
    if (tripPrice.getTrip() == null || tripPrice.getTrip().getId() != tripId || tripPrice.getAccommodation() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel option not found for this trip");
    }
    return tripPrice;
  }

  private FlightOptionDTO toFlightDto(TripPrice tripPrice) {
    Flight flight = tripPrice.getFlight();
    return new FlightOptionDTO(
        tripPrice.getId(),
        tripPrice.getTripprice_provider(),
        tripPrice.getPrice() == null ? 0 : tripPrice.getPrice().doubleValue(),
        flight == null ? null : flight.getId(),
        flight == null ? null : flight.getAirline(),
        flight == null ? null : flight.getDeparture_city(),
        flight == null ? null : flight.getDestination_city(),
        flight == null ? null : flight.getDeparture_airport(),
        flight == null ? null : flight.getDestination_airport(),
        flight == null ? null : flight.getFlight_duration());
  }

  private HotelOptionDTO toHotelDto(TripPrice tripPrice) {
    Accommodation accommodation = tripPrice.getAccommodation();
    return new HotelOptionDTO(
        tripPrice.getId(),
        tripPrice.getTripprice_provider(),
        tripPrice.getPrice() == null ? 0 : tripPrice.getPrice().doubleValue(),
        accommodation == null ? null : accommodation.getId(),
        accommodation == null ? null : accommodation.getHotel_name(),
        accommodation == null ? null : accommodation.getHotel_type(),
        accommodation == null ? null : accommodation.getHotel_city(),
        accommodation == null ? null : accommodation.getHotel_location(),
        accommodation == null ? null : accommodation.getAmenities(),
        accommodation == null ? 0 : accommodation.getNights(),
        accommodation == null ? 0 : accommodation.getLatitude(),
        accommodation == null ? 0 : accommodation.getLongitude());
  }
}
