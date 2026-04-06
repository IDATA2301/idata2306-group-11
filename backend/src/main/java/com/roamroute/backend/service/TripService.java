package com.roamroute.backend.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.roamroute.backend.dto.TripDetailsDTO;
import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.dto.TripPriceDTO;
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

        List<Trip> trips = tripRepository.findTop3ByOrderByIdAsc();

        return trips.stream().map(trip -> {

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

            return new TripHomeDTO(
                trip.getId(),
                trip.getTitle(),
                trip.getImage_url(),
                trip.getDestination().getCity(),
                trip.getDestination().getCountry(),
                lowestFlight + lowestHotel
            );

        }).toList();
    }

    public TripDetailsDTO getTripDetails(int id) {
        Trip trip = tripRepository.findById(id);
        
        String flightDuration = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .findFirst()
            .map(tp -> tp.getFlight().getFlight_duration())
            .orElse("N/A");

        List<TripPriceDTO> flightOptions = tripPriceRepository
            .findByTrip_IdAndFlightIsNotNull(id)
            .stream()
            .map(tp -> new TripPriceDTO(tp.getId(), tp.getTripprice_provider(), tp.getPrice().doubleValue()))
            .sorted(Comparator.comparingDouble(TripPriceDTO::getPrice)) 
            .toList();

        List<TripPriceDTO> hotelOptions = tripPriceRepository
            .findByTrip_IdAndAccommodationIsNotNull(id)
            .stream()
            .map(tp -> new TripPriceDTO(tp.getId(), tp.getTripprice_provider(), tp.getPrice().doubleValue()))
            .sorted(Comparator.comparingDouble(TripPriceDTO::getPrice))
            .toList();

        return new TripDetailsDTO(  
            trip.getId(),
            trip.getTitle(),
            trip.getTrip_description(),
            trip.getImage_url(),
            trip.getDestination().getCity(),
            trip.getDestination().getCountry(),
            trip.getStart_date().toString(),
            trip.getEnd_date().toString(),
            flightDuration,
            flightOptions,
            hotelOptions
        );
    }
}
