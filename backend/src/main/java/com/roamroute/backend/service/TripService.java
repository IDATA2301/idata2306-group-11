package com.roamroute.backend.service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.roamroute.backend.dto.CreateTripRequest;
import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.dto.TripPriceDTO;
import com.roamroute.backend.dto.UpdateTripRequest;
import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.repository.DestinationRepository;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.SelectedPackageRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
/**
 * Service for managing trips including CRUD operations, search, and retrieval of trip details with associated pricing options.
 */
public class TripService {

    private final TripRepository tripRepository;
    private final TripPriceRepository tripPriceRepository;
    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;
    private final SelectedPackageRepository selectedPackageRepository;

    public TripService(TripRepository tripRepository,
                       TripPriceRepository tripPriceRepository,
                       DestinationRepository destinationRepository,
                       OrderRepository orderRepository,
                       SelectedPackageRepository selectedPackageRepository) {
        this.tripRepository = tripRepository;
        this.tripPriceRepository = tripPriceRepository;
        this.destinationRepository = destinationRepository;
        this.orderRepository = orderRepository;
        this.selectedPackageRepository = selectedPackageRepository;
    }

    public List<TripHomeDTO> getHomeTrips() {
        return tripRepository.find3RandomActive().stream()
            .map(this::toTripHomeDTO)
            .toList();
    }

    public TripDetailsDTO getTripDetails(int id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found")
        );

        if (!trip.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }

        String flightDuration = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .findFirst()
            .map(tp -> tp.getFlight().getFlight_duration())
            .orElse("N/A");

        List<TripPriceDTO> flightOptions = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .map(tp -> new TripPriceDTO(
                tp.getId(),
                tp.getTripprice_provider(),
                tp.getPrice().doubleValue(),
                tp.getFlight().getAirline()))
            .sorted(Comparator.comparingDouble(TripPriceDTO::getPrice)) 
            .toList();

        List<TripPriceDTO> hotelOptions = tripPriceRepository
            .findByTrip_IdAndAccommodationIsNotNull(id)
            .stream()
            .map(tp -> new TripPriceDTO(tp.getId(), tp.getTripprice_provider(), tp.getPrice().doubleValue()))
            .sorted(Comparator.comparingDouble(TripPriceDTO::getPrice))
            .toList();

        var departureAirport = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .findFirst()
            .map(tp -> tp.getFlight().getDeparture_airport())
            .orElse("N/A");

        var arrivalAirport = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .findFirst()
            .map(tp -> tp.getFlight().getDestination_airport())
            .orElse("N/A");

        List<String> keywords = List.of(trip.getKeywords().split(",")).stream()
            .map(String::trim)
            .toList();

        var accommodation = tripPriceRepository
            .findByTrip_IdAndAccommodationIsNotNull(id)
            .stream()
            .findFirst()
            .map(tp -> tp.getAccommodation())
            .orElse(null);

        String hotelName = accommodation != null ? accommodation.getHotel_name() : "N/A";
        String hotelType = accommodation != null ? accommodation.getHotel_type() : "N/A";
        String hotelLocation = accommodation != null ? accommodation.getHotel_location() : "N/A";
        String amenities = accommodation != null ? accommodation.getAmenities() : "N/A";
        int nights = accommodation != null ? accommodation.getNights() : 0;
        double latitude = accommodation != null ? accommodation.getLatitude() : 0;
        double longitude = accommodation != null ? accommodation.getLongitude() : 0;

        var destination = trip.getDestination();
        Integer destinationId = destination != null ? destination.getId() : null;
        String city = destination != null ? destination.getCity() : "";
        String country = destination != null ? destination.getCountry() : "";
        String destinationImageUrl = destination != null ? destination.getImage_url() : null;
        String destinationImageAlt = destination != null ? destination.getImage_alt() : null;

        boolean active = trip.isActive();

        return new TripDetailsDTO(
            trip.getId(),
            trip.getTitle(),
            trip.getTrip_description(),
            trip.getImage_url(),
            destinationId,
            city,
            country,
            destinationImageUrl,
            destinationImageAlt,
            trip.getStart_date() != null ? trip.getStart_date().toString() : null,
            trip.getEnd_date() != null ? trip.getEnd_date().toString() : null,
            flightDuration,
            flightOptions,
            hotelOptions,
            departureAirport,
            arrivalAirport,
            keywords,
            hotelName,
            hotelType,
            hotelLocation,
            amenities,
            nights,
            latitude,
            longitude,
            active
        );
    }

    public TripDetailsDTO createTrip(CreateTripRequest request) {
        int nextId = tripRepository.findMaxId() + 1;

        Destination destination = null;
        if (request.getDestinationId() != null) {
            destination = destinationRepository.findById(request.getDestinationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination not found"));
        }

        Trip trip = new Trip();
        trip.setId(nextId);
        trip.setTitle(request.getTitle());
        trip.setTrip_description(request.getDescription());
        trip.setImage_url(request.getImageUrl());
        trip.setStart_date(request.getStartDate() != null ? Date.valueOf(request.getStartDate()) : null);
        trip.setEnd_date(request.getEndDate() != null ? Date.valueOf(request.getEndDate()) : null);
        trip.setKeywords(request.getKeywords() != null ? String.join(",", request.getKeywords()) : null);
        trip.setDestination(destination);
        trip.setActive(true);

        tripRepository.save(trip);
        return getTripDetails(nextId);
    }

    public TripDetailsDTO updateTrip(int id, UpdateTripRequest request) {
        Trip trip = tripRepository.findById(id).orElseThrow();

        if (request.getTitle() != null) {
            trip.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            trip.setTrip_description(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            trip.setImage_url(request.getImageUrl());
        }
        if (request.getStartDate() != null) {
            trip.setStart_date(Date.valueOf(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            trip.setEnd_date(Date.valueOf(request.getEndDate()));
        }
        if (request.getKeywords() != null) {
            trip.setKeywords(String.join(",", request.getKeywords()));
        }

        tripRepository.save(trip);

        return getTripDetails(id);
    }

    public List<TripHomeDTO> searchTrips(String query, Double minPrice, Double maxPrice, Integer destinationId) {
        String normalizedQuery = query == null ? "" : query.trim();

        List<Trip> trips = normalizedQuery.isEmpty()
            ? tripRepository.findByActiveTrue()
            : tripRepository.searchActiveTrips(normalizedQuery);

        return trips.stream()
            .filter(t -> destinationId == null
                || (t.getDestination() != null && t.getDestination().getId() == destinationId))
            .map(this::toTripHomeDTO)
            .filter(dto -> minPrice == null || dto.getLowestPrice() >= minPrice)
            .filter(dto -> maxPrice == null || dto.getLowestPrice() <= maxPrice)
            .toList();
    }

    public TripHomeDTO toTripHomeDTO(Trip trip) {
        double lowestFlight = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(trip.getId())
            .stream()
            .mapToDouble(tp -> tp.getPrice().doubleValue())
            .min()
            .orElse(0);

        double lowestHotel = tripPriceRepository
            .findByTrip_IdAndAccommodationIsNotNull(trip.getId())
            .stream()
            .mapToDouble(tp -> tp.getPrice().doubleValue())
            .min()
            .orElse(0);

        Date startDate = trip.getStart_date();
        Date endDate = trip.getEnd_date();

        boolean active = trip.isActive();

        return new TripHomeDTO(
            trip.getId(),
            trip.getTitle(),
            trip.getImage_url(),
            trip.getDestination().getCity(),
            trip.getDestination().getCountry(),
            lowestFlight + lowestHotel,
            startDate,
            endDate,
            active
        );
    }

    public List<TripHomeDTO> getAllTripsForAdmin() {
        return tripRepository.findAll().stream()
            .map(this::toTripHomeDTO)
            .toList();
    }

    @Transactional
    public void deleteTrip(int id) {
        if (!tripRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        if (orderRepository.existsByTrip_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Cannot delete a trip that has existing orders");
        }
        selectedPackageRepository.deleteByTrip_Id(id);
        tripPriceRepository.deleteByTrip_Id(id);
        tripRepository.deleteById(id);
    }
}
