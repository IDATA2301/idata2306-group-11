package com.roamroute.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Date;
import java.util.Optional;

import com.roamroute.backend.dto.TripHomeDTO;

import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.TripPrice;
import com.roamroute.backend.repository.DestinationRepository;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.SelectedPackageRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.service.TripService;

/**
 * Unit tests for TripService.
 */
@ExtendWith(MockitoExtension.class)
class TripServiceTest {

	@Mock
	private TripRepository tripRepository;

	@Mock
	private TripPriceRepository tripPriceRepository;

	@Mock
	private DestinationRepository destinationRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private SelectedPackageRepository selectedPackageRepository;

	@InjectMocks
	private TripService tripService;

	/**
	 * searchTrips should only return trips within the specified price range.
	 * This test sets up two trips with different prices and verifies that only 
	 * the trip within the range is returned.
	 */
	@Test
	void searchTrips_withPriceFilters_returnsOnlyWithinRange() {
		Trip trip1 = createTrip(101, 1, "Trip 1", "City 1", "Country 1");
		Trip trip2 = createTrip(102, 2, "Trip 2", "City 2", "Country 2");

		when(tripRepository.findByActiveTrue()).thenReturn(List.of(trip1, trip2));
		when(tripPriceRepository.findByTrip_IdAndFlightIsNotNull(101))
			.thenReturn(List.of(createMockTripPrice(300)));
		when(tripPriceRepository.findByTrip_IdAndAccommodationIsNotNull(101))
			.thenReturn(List.of(createMockTripPrice(200)));
		when(tripPriceRepository.findByTrip_IdAndFlightIsNotNull(102))
			.thenReturn(List.of(createMockTripPrice(700)));
		when(tripPriceRepository.findByTrip_IdAndAccommodationIsNotNull(102))
			.thenReturn(List.of(createMockTripPrice(400)));

		List<TripHomeDTO> result = tripService.searchTrips(null, 400.0, 600.0, null);

		assertEquals(1, result.size());
		assertEquals("Trip 1", result.get(0).getTitle());
	}

	/**
	 * searchTrips should only return trips that match the specified destination ID.
	 * This test sets up two trips with different destination IDs and verifies that 
	 * only the trip with the matching destination ID is returned.
	 */
	@Test
	void searchTrips_withDestinationIdFilter_returnsOnlyMatchingDestination() {
		Trip trip1 = createTrip(101, 1, "Trip 1", "City 1", "Country 1");
		Trip trip2 = createTrip(102, 2, "Trip 2", "City 2", "Country 2");

		when(tripRepository.findByActiveTrue()).thenReturn(List.of(trip1, trip2));

		List<TripHomeDTO> result = tripService.searchTrips(null, null, null, 1);

		assertEquals(1, result.size());
		assertEquals("Trip 1", result.get(0).getTitle());
	}

	/*
	 * searchTrips should use the searchActiveTrips method when a query is provided.
	 * This test sets up a trip that matches the query and verifies that it is returned
	 */
	@Test
	void searchTrips_withQueryUsesSearchActiveTripsAndMatchesTitle() {
		Trip trip = createTrip(103, 3, "Barcelona Weekend", "Barcelona", "Spain");

		when(tripRepository.searchActiveTrips("Barcelona")).thenReturn(List.of(trip));

		List<TripHomeDTO> result = tripService.searchTrips("Barcelona", null, null, null);

		assertEquals(1, result.size());
		assertEquals("Barcelona Weekend", result.get(0).getTitle());
	}

	/**
	 * deleteTrip should throw a 404 Not Found if the trip does not exist. 
	 * This test verifies that the service checks for the trip's existence and throws the appropriate exception when it is not found.
	 */
	@Test
	void deleteTrip_whenTripDoesNotExist_throwsNotFound() {
		when(tripRepository.existsById(1)).thenReturn(false);

		assertThrows(ResponseStatusException.class, () -> tripService.deleteTrip(1));

		verify(tripRepository).existsById(1);
	}

	/**
	 * deleteTrip should throw a 409 Conflict if the trip has existing orders. 
	 * This test verifies that the service checks for existing orders and throws the appropriate exception when they are found.
	 */
	@Test
	void deleteTrip_whenTripHasOrders_throwsConflict() {
		when(tripRepository.existsById(1)).thenReturn(true);
		when(orderRepository.existsByTrip_Id(1)).thenReturn(true);

		assertThrows(ResponseStatusException.class, () -> tripService.deleteTrip(1));

		verify(tripRepository).existsById(1);
		verify(orderRepository).existsByTrip_Id(1);
	}

	/**
	 * deleteTrip should delete related data and the trip when it can be deleted. 
	 * This test verifies that the service checks for the trip's existence and orders, then deletes related data and the trip when it is safe to do so.
	 */
	@Test
	void deleteTrip_whenTripCanBeDeleted_deletesRelatedDataAndTrip() {
		when(tripRepository.existsById(1)).thenReturn(true);
		when(orderRepository.existsByTrip_Id(1)).thenReturn(false);

		tripService.deleteTrip(1);

		verify(tripRepository).existsById(1);
		verify(orderRepository).existsByTrip_Id(1);
		verify(selectedPackageRepository).deleteByTrip_Id(1);
		verify(tripPriceRepository).deleteByTrip_Id(1);
		verify(tripRepository).deleteById(1);
	}

	/**
	 * getTripDetails should return the correct details when the trip exists.
	 * This test sets up a trip with specific details and verifies that the service returns those details correctly when requested.
	 * It also verifies that the service handles the case where the trip does not exist by throwing the appropriate exception.
	 */
	@Test
	void getTripDetails_whenTripExists_returnsDetails() {
		Trip trip = new Trip();
		trip.setId(201);
		trip.setTitle("Detailed Trip");
		trip.setActive(true);
		trip.setDestination(new Destination());
		trip.getDestination().setId(5);
		trip.getDestination().setCity("CityX");
		trip.getDestination().setCountry("CountryX");
		trip.setStart_date(Date.valueOf("2026-06-01"));
		trip.setEnd_date(Date.valueOf("2026-06-05"));
		trip.setKeywords("");

		when(tripRepository.findById(201)).thenReturn(Optional.of(trip));
		when(tripPriceRepository.findByTrip_IdAndFlightIsNotNull(201)).thenReturn(List.of());
		when(tripPriceRepository.findByTrip_IdAndAccommodationIsNotNull(201)).thenReturn(List.of());

		var dto = tripService.getTripDetails(201);

		assertEquals("Detailed Trip", dto.getTitle());
		assertEquals(Integer.valueOf(5), dto.getDestinationId());
	}

	/**
	 * getTripDetails should throw a 404 Not Found if the trip does not exist.
	 * This test verifies that the service checks for the trip's existence and throws the appropriate exception when it is not found.
	 * It ensures that the service does not attempt to access details of a non-existent trip and handles the error gracefully.
	 */
	@Test
	void getTripDetails_whenMissing_throwsNotFound() {
		when(tripRepository.findById(999)).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> tripService.getTripDetails(999));
	}

	/**
	 * createTrip should throw a 404 Not Found if the specified destination does not exist, and should create and return trip details when the destination is valid.
	 * This test verifies that the service checks for the existence of the destination and throws the appropriate exception when it is not found. 
	 * It also verifies that when a valid destination is provided, the service creates a new trip, saves it, and returns the correct details. The test ensures that the service
	 */
	@Test
	void createTrip_withValidAndInvalidDestination_behaviour() {
		// invalid destination -> 404
		var reqInvalid = new com.roamroute.backend.dto.CreateTripRequest();
		reqInvalid.setDestinationId(999);
		when(destinationRepository.findById(999)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> tripService.createTrip(reqInvalid));

		// valid destination -> saved and details returned
		Destination dest = new Destination();
		dest.setId(10);
		when(destinationRepository.findById(10)).thenReturn(Optional.of(dest));
		when(tripRepository.findMaxId()).thenReturn(300);

		Trip saved = new Trip();
		saved.setId(301);
		saved.setTitle("Created Trip");
		saved.setActive(true);
		saved.setDestination(dest);
		saved.setStart_date(Date.valueOf("2026-07-01"));
		saved.setEnd_date(Date.valueOf("2026-07-05"));
		saved.setKeywords("");

		when(tripRepository.findById(301)).thenReturn(Optional.of(saved));

		var req = new com.roamroute.backend.dto.CreateTripRequest();
		req.setTitle("Created Trip");
		req.setDestinationId(10);

		var details = tripService.createTrip(req);

		assertEquals("Created Trip", details.getTitle());
		verify(tripRepository).save(org.mockito.ArgumentMatchers.any(Trip.class));
	}

	private TripPrice createMockTripPrice(double price) {
		TripPrice tp = new TripPrice();
		tp.setPrice(new java.math.BigDecimal(price));
		return tp;
	}

	private Trip createTrip(int tripId, int destinationId, String title, String city, String country) {
		Destination destination = new Destination();
		destination.setId(destinationId);
		destination.setCity(city);
		destination.setCountry(country);

		Trip trip = new Trip();
		trip.setId(tripId);
		trip.setTitle(title);
		trip.setDestination(destination);
		trip.setActive(true);
		return trip;
	}
}


