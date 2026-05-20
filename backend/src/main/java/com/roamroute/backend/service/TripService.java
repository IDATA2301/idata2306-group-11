package com.roamroute.backend.service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.dto.TripPriceDTO;
import com.roamroute.backend.dto.UpdateTripRequest;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;


@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripPriceRepository tripPriceRepository;

    public TripService(TripRepository tripRepository,
                       TripPriceRepository tripPriceRepository) {
        this.tripRepository = tripRepository;
        this.tripPriceRepository = tripPriceRepository;
    }

    public List<TripHomeDTO> getHomeTrips() {
        return tripRepository.findTop3ByOrderByIdAsc().stream()
            .map(this::toTripHomeDTO)
            .toList();
    }

    public TripDetailsDTO getTripDetails(int id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found")
        );
    
        
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
            trip.getStart_date().toString(),
            trip.getEnd_date().toString(),
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
}
